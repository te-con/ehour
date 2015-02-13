package net.rrm.ehour.persistence.project.dao

import javax.persistence.{ AttributeConverter, Converter }

import net.rrm.ehour.domain.ProjectAssignmentType

@Converter(autoApply = true)
class ProjectAssignmentTypeConverter extends AttributeConverter[ProjectAssignmentType, Integer] {
  override def convertToDatabaseColumn(attribute: ProjectAssignmentType): Integer = {
    attribute.getId
  }

  override def convertToEntityAttribute(dbData: Integer): ProjectAssignmentType = {
    ProjectAssignmentType.findById(dbData)
  }
}
