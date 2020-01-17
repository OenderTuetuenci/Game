package model.fileIOComponent.fileIOJsonImpl

import Game.MonopolyModule
import com.google.inject.Guice
import controller.controllerComponent.GameControllerInterface
import model.{Buyable, Cards, CardsInterface, Cell, CommunityChest, Eventcell, FreiParken, GoToJail, IncomeTax, Jail, Los, PlayerInterface, Street, Zusatzsteuer}
import model.fileIOComponent.FileIOInterface
import model.playerComponent.Player
import play.api.libs.json.{JsNumber, JsObject, JsValue, Json, Writes}

import scala.io.Source

class FileIO extends FileIOInterface {

    override def loadGame: (Int,Int,Int,Int,Int,Vector[Cell],Vector[PlayerInterface],Vector[String],Vector[String],Int) = {
        val source: String = Source.fromFile("game.json").getLines.mkString
        val json: JsValue = Json.parse(source)
        var chanceCards = Vector[String]()
        var communityChestCards = Vector[String]()
        for(index <- 0 until 11){
            val card = (json \\"chancecard")(index).toString()
            chanceCards = chanceCards :+ card
        }
        for(index <- 0 until 14){
            val card = (json \\"communitycard")(index).toString()
            communityChestCards = communityChestCards:+card
        }
        val humanplayer = (json \ "game" \"humanPlayers").get.toString().toInt
        val npcplayer = (json \ "game" \"npcPlayers").get.toString().toInt
        var board = Vector[Cell]()
        for(index <- 0 until 40){
            val cell = (json\\"cell")(index)
            val kind = (cell\"kind").as[String]
            val name = (cell\"name").as[String]
            val group = (cell\"group").as[Int]
            var mortgage = false
            var price = 0
            var rent = 0
            var owner = -1
            var homecount = 0
            if(kind == "buyable"){
                mortgage = (cell\"mortgage").as[Boolean]
                price = (cell\"price").as[Int]
                rent = (cell\"rent").as[Int]
                owner = (cell\"owner").as[Int]
                homecount = (cell\"homecount").as[Int]
            }
            }
        var player = Vector[PlayerInterface]()
        val dataPlayer = (json \ "game" \ "players").get.toString().replace("\"card\":","")
          .replace("[","").replace("]","").replace("\"player\":",";")
          .replace("{","").replace("\"","").replace("}","")
          .replace(",","").split(";")
        for(i<-1 until dataPlayer.length){
            val t = dataPlayer(i).replace("name:","").replace("money:",";")
              .replace("jailCount:",";").replace("turnPosition:",";").replace("rollForPosition:",";")
              .replace("figure:",";").replace("position:",";").split(";")
            player = player :+ Player(t(0),t(1).toInt,t(2).toInt,t(3).toInt,t(4).toInt,t(5).toInt,t(6))
        }
        val round = (json \ "game" \ "round").get.toString.toInt
        val paschCount = (json \ "game" \ "paschCount").get.toString.toInt
        val collectedTax = (json \ "game" \ "collectedTax").get.toString.toInt
        val currentPlayer = (json \ "game" \ "currentPlayer").get.toString.toInt
        (humanplayer,npcplayer,round,paschCount,collectedTax,board,player,chanceCards,communityChestCards,currentPlayer)
    }

    override def saveGame(game:GameControllerInterface): Unit = {
        import java.io._
        val pw = new PrintWriter(new File("game.json"))
        pw.write(Json.prettyPrint(gameToJson(game)))
        pw.close
    }
    implicit val playerWrites = new Writes[PlayerInterface] {
        override def writes(player: PlayerInterface):JsValue = Json.obj(
            "name" -> player.name,
            "position" -> player.position,
            "money" -> player.money,
            "jailCount"->player.jailCount,
            "turnPosition"->player.turnPosition,
            "rollForPosition"->player.rollForPosition,
            "figure"->player.figure
        )
    }
    implicit val cellWrites = new Writes[Cell] {
        override def writes(cell: Cell):JsValue =
            cell match {
                case cell:Buyable => Json.obj(
                    "kind"->"buyable",
                    "name"->cell.name,
                    "group"->cell.group,
                    "mortgage"->cell.mortgage,
                    "price"->cell.price,
                    "rent"->cell.rent,
                    "owner"->cell.owner,
                    "homecount"->cell.homecount,
                    "image"->cell.image
                )
                case cell:Cell => Json.obj(
                    "kind"->"cell",
                    "name"->cell.name,
                    "group"->cell.group
                )
            }
    }
    def gameToJson(game: GameControllerInterface): JsObject = {
        Json.obj(
            "game" -> Json.obj(
                "chanceCards"-> Json.arr(
                    for{
                        i<-game.chanceCards.indices
                    }yield {
                        Json.obj(
                            "chancecard"->Json.toJson(game.chanceCards(i))
                        )
                    }
                ),
                "communityChestCards"-> Json.arr(
                    for{
                        i<-game.communityChestCards.indices
                    }yield {
                        Json.obj(
                            "communitycard"->Json.toJson(game.communityChestCards(i))
                        )
                    }
                ),
                "humanPlayers"->JsNumber(game.humanPlayers),
                "npcPlayers"->JsNumber(game.npcPlayers),
                "board"-> Json.toJson(
                    for{
                        i<-game.board.indices
                    }yield {
                        Json.obj(
                            "cell"->Json.toJson(game.board(i))
                        )
                    }
                ),
                "players"->Json.toJson(
                    for{
                        i<-game.players.indices
                    }yield {
                        Json.obj(
                            "player"->Json.toJson(game.players(i))
                        )
                    }
                ),
                "round"->JsNumber(game.round),
                "paschCount"->JsNumber(game.paschCount),
                "collectedTax"->JsNumber(game.collectedTax),
                "currentPlayer"->JsNumber(game.currentPlayer)
            )
        )
    }
    object CellFactory {
        def apply(kind: String, name: String, group: Int, price: Int, owner: Int, rent: Int, home: Int, mortgage: Boolean,
                  image: String): Cell = kind match {
            case "Los" => Los(name, group, image: String)
            case "Street" => Street(name, group, price, owner, rent, home, mortgage, image: String)
            case "CommunityChest" => CommunityChest(name, group)
            case "IncomeTax" => IncomeTax(name, group, image: String)
            case "Eventcell" => Eventcell(name, group)
            case "Jail" => Jail(name, group, image: String)
            case "FreeParking" => FreiParken(name, group, image: String)
            case "GoToJail" => GoToJail(name, group, image: String)
            case "AdditionalTax" => Zusatzsteuer(name, group, image: String)
            case _ => throw new UnsupportedOperationException
        }
    }
}