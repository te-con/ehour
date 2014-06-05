package net.rrm.ehour.ui.common.component

import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.html.form.DropDownChoice
import org.apache.wicket.markup.html.form.IChoiceRenderer
import org.apache.wicket.model.IModel
import org.apache.wicket.util.string.AppendingStringBuffer
import java.util

trait OptGroupRenderer[T] extends IChoiceRenderer[T] {
  def getOptGroupLabel(t: T): String
}

abstract class OptGroupRendererImpl[T](optGroup: util.Map[T, String]) extends OptGroupRenderer[T] {
  override def getOptGroupLabel(t: T): String = optGroup.get(t)
}

class GroupableDropDownChoice[T](id: String, model: IModel[T], choices: IModel[_ <: util.List[_ <: T]], renderer: IChoiceRenderer[_ >: T]) extends DropDownChoice[T](id, model, choices, renderer) {
  private var previouslyAppendedOptGroupLabel: String = null
  private var choiceCount: Int = 0
  private val OptionOpenHtml = "<option>"
  private val OptionCloseHtml = "</option>"

  override def onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) {
    previouslyAppendedOptGroupLabel = null
    choiceCount = getChoices.size

    super.onComponentTagBody(markupStream, openTag)
  }

  @SuppressWarnings(Array("unchecked"))
  protected override def appendOptionHtml(buffer: AppendingStringBuffer, choice: T, index: Int, selected: String) {
    val buffer = new AppendingStringBuffer(50)
    super.appendOptionHtml(buffer, choice, index, selected)

    def applyRender(renderer: (IChoiceRenderer[T] with OptGroupRenderer[T])): String = {
      val currentOptGroupLabel = renderer.getOptGroupLabel(choice)

      if (!equalsOrNull(currentOptGroupLabel, previouslyAppendedOptGroupLabel)) {
        if (previouslyAppendedOptGroupLabel != null) {
          endOptGroup(buffer)
        }

        if (currentOptGroupLabel != null) {
          val start = buffer.indexOf(OptionOpenHtml)
          buffer.insert(start, s"<optgroup label=\"$currentOptGroupLabel\">")
        }
      }

      if ((currentOptGroupLabel != null) && (index == (choiceCount - 1))) {
        endOptGroup(buffer)
      }

      currentOptGroupLabel
    }
    getChoiceRenderer match {
      case styledChoiceRenderer: OptGroupRenderer[T] =>
        val currentOptGroupLabel = applyRender(styledChoiceRenderer)
        previouslyAppendedOptGroupLabel = currentOptGroupLabel
      case _ =>
    }
    buffer.append(buffer)
  }

  private def equalsOrNull(a: AnyRef, b: AnyRef): Boolean = b == null || (b == a)

  private def endOptGroup(tmp: AppendingStringBuffer) {
    val start = tmp.indexOf(OptionCloseHtml)
    tmp.insert(start + OptionCloseHtml.length, "</optgroup>")
  }
}



