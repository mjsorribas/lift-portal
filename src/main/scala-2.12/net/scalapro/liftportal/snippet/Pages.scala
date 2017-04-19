package net.scalapro.liftportal.snippet

import net.liftweb.common.Full
import net.liftweb.http.{S, SHtml}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.views.{PageV, TemplateV}
import net.scalapro.liftportal.util.DB

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import net.scalapro.liftportal.util.Tags
import net.scalapro.liftportal.util.Vars.templateId


object Pages {

  private def removePage(id: String): Unit = {
    if (id == "1") {
      S.error("error", <div class="alert alert-danger" role="alert">You can't remove the default template!</div>)
      return
    }
    val q = TemplateV.view.filter(_.id === id.toInt)
    val action = q.delete
    val db = DB.getDatabase
    try {
      Await.result(
        db.run(action)
        , Duration.Inf)
    }
    finally {
      db.close
      S.redirectTo("templates")
    }
  }


  def render = {
    S.param("d") match {
      case Full(x) => removePage(templateId(x))
      case _ =>
    }
    val db = DB.getDatabase
    try {
      val q = PageV.view.map(o => o)
      val action = q.result

      Await.result(
        db.run(action) // Future[Seq[String]]
          .map { p =>

          ".rows" #> p.map(x => {
            <tr>
              {Tags.td(x.title, None, Some("edit-page?p=" + x.id.getOrElse("") + ""))}
              {Tags.td(x.description.getOrElse(""))}
              <td>
                {SHtml.link("", ()=>{
                removePage(x.id.getOrElse(1).toString)
              }, <span class="glyphicon glyphicon-remove"></span>,
                "class"->"btn btn-default btn-sm")
                }
                {SHtml.link("template-preview", ()=>{
                templateId(x.id.getOrElse(1).toString)
              }, <span class="glyphicon glyphicon-search"></span>,
                "class"->"btn btn-default btn-sm")
                }

                {SHtml.link("template", ()=>{
                templateId(x.id.getOrElse(1).toString)
              }, <span class="glyphicon glyphicon-th"></span>,
                "class"->"btn btn-default btn-sm")
                }

              </td>
            </tr>
          }
          )
        }, Duration(2, "second")
      )

    }
    finally {
      db.close
    }
  }
}