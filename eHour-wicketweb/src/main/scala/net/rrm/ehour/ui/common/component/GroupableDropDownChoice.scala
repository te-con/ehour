package net.rrm.ehour.ui.common.component

import java.util

import org.apache.wicket.markup.{ComponentTag, MarkupStream}
import org.apache.wicket.markup.html.form.{AbstractChoice, DropDownChoice, IChoiceRenderer, ListMultipleChoice}
import org.apache.wicket.model.IModel
import org.apache.wicket.util.string.AppendingStringBuffer

// Thanks to Martin Makundi for the original Java code

trait OptGroupRenderer[T] extends IChoiceRenderer[T] {
  def getOptGroupLabel(t: T): String
}

abstract class OptGroupRendererMap[T](optGroup: util.Map[T, String]) extends OptGroupRenderer[T] {
  override def getOptGroupLabel(t: T): String = optGroup.get(t)
}

trait GroupableChoice[T, E] extends AbstractChoice[T, E] {
  private var currentlyActiveOptGroupLabel: Option[String] = None
  private var choiceCount: Int = 0
  private val OptionOpenHtml = "<option"
  private val OptionCloseHtml = "</option>"

  override def onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) {
    currentlyActiveOptGroupLabel = None
    choiceCount = getChoices.size

    super.onComponentTagBody(markupStream, openTag)
  }

  @SuppressWarnings(Array("unchecked"))
  protected override def appendOptionHtml(buffer: AppendingStringBuffer, choice: E, index: Int, selected: String) {
    val appendingBuffer = new AppendingStringBuffer(1024)
    super.appendOptionHtml(appendingBuffer, choice, index, selected)

    def applyRenderer(renderer: (IChoiceRenderer[E] with OptGroupRenderer[E])): Option[String] = {
      val currentOptGroupLabel = Option.apply(renderer.getOptGroupLabel(choice))

      if (currentOptGroupLabel != currentlyActiveOptGroupLabel) {
        if (currentlyActiveOptGroupLabel.isDefined) {
          endOptGroup(buffer)
        }

        if (currentOptGroupLabel.isDefined) {
          val start = appendingBuffer.indexOf(OptionOpenHtml)
          val label = currentOptGroupLabel.get
          appendingBuffer.insert(start, s"""<optgroup label=\"$label\">""")
        }
      }

      if (currentOptGroupLabel.isDefined && (index == (choiceCount - 1))) {
        endOptGroup(appendingBuffer)
      }

      currentOptGroupLabel
    }

    getChoiceRenderer match {
      case optGroupRenderer: OptGroupRenderer[E] =>
        currentlyActiveOptGroupLabel = applyRenderer(optGroupRenderer)
      case _ =>
    }
    buffer.append(appendingBuffer)
  }


  private def endOptGroup(buffer: AppendingStringBuffer) {
    val start = buffer.indexOf(OptionCloseHtml)
    buffer.insert(start + OptionCloseHtml.length, "</optgroup>")
  }
}

class GroupableDropDownChoice[T](id: String, model: IModel[T], choices: IModel[_ <: util.List[_ <: T]], renderer: IChoiceRenderer[_ >: T])
  extends DropDownChoice[T](id, model, choices, renderer)
  with GroupableChoice[T, T]

class GroupableListMultipleChoice[T](id: String, model: IModel[util.Collection[T]], choices: IModel[_ <: util.List[_ <: T]], renderer: IChoiceRenderer[_ >: T])
  extends ListMultipleChoice[T](id, model, choices, renderer)
  with GroupableChoice[util.Collection[T], T]