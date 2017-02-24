package net.scalapro.liftportal.cms.tables

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.{Nullable, SqlType}

case class PWidget (
                     id: Option[Int],
                     widget_id: Int,
                     template_id: Int,
                     space_id: Int,
                     p_container_id: Int,
                     params: Option[String],
                     ord: Int,
                     widget_class: Option[String]
                   )
object PWidget {

  class PWidgets(tag: Tag) extends Table[PWidget](tag, "p_widgets") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def widget_id = column[Int]("widget_id")

    def page_id = column[Int]("page_id")

    def space_id = column[Int]("space_id")

    def p_container_id = column[Int]("p_container_id")

    def ord = column[Int]("ord")

    def widget_class = column[Option[String]]("container_class", Nullable)

    def params = column[Option[String]]("params", SqlType("TEXT"))


    def * = (id.?, widget_id, page_id, space_id, p_container_id, params, ord, widget_class) <>
      ((PWidget.apply _).tupled, PWidget.unapply)

    def widget_fk = foreignKey("widget_fk", widget_id, Widget.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def page_fk = foreignKey("page_fk", page_id, Page.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def space_fk = foreignKey("space_fk", space_id, Space.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def p_container_fk = foreignKey("p_container_fk", p_container_id, PContainer.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }


  val table = TableQuery[PWidgets]

}