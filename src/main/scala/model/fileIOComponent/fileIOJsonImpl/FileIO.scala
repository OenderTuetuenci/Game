package model.fileIOComponent.fileIOJsonImpl

import controller.controllerComponent.ControllerInterface
import model.fileIOComponent.FileIOInterface
import model.playerComponent.Player
import model._
import play.api.libs.json._

import scala.io.Source

class FileIO extends FileIOInterface {

    override def loadGame: (Int, Int, Int, Int, Int, Vector[Cell], Vector[PlayerInterface], Vector[String], Vector[String], Int) = {
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
            val image = (cell\"image").as[String]
            if(kind == "Street"){
                mortgage = (cell\"mortgage").as[Boolean]
                price = (cell\"price").as[Int]
                rent = (cell\"rent").as[Int]
                owner = (cell\"owner").as[Int]
                homecount = (cell\"homecount").as[Int]
            }
            board = board :+ CellFactory(kind,name,group,price,owner,rent,homecount,mortgage = mortgage,image)
            }
        var players = Vector[PlayerInterface]()
        val playerCount = (json\"game"\"humanPlayers").get.toString().toInt
        for(index <- 0 until playerCount){
            val player = (json\\"player")(index)
            val name = (player \ "name").as[String]
            val position = (player \ "position").as[Int]
            val money = (player \ "money").as[Int]
            val jailCount = (player \ "jailCount").as[Int]
            val turnPosition = (player \ "turnPosition").as[Int]
            val rollForPosition = (player \ "rollForPosition").as[Int]
            val figure = (player \ "figure").as[String]
            players = players :+ Player(name,position,money,jailCount,turnPosition,rollForPosition,figure)
        }
        val round = (json \ "game" \ "round").get.toString.toInt
        val paschCount = (json \ "game" \ "paschCount").get.toString.toInt
        val collectedTax = (json \ "game" \ "collectedTax").get.toString.toInt
        val currentPlayer = (json \ "game" \ "currentPlayer").get.toString.toInt
        (humanplayer,npcplayer,round,paschCount,collectedTax,board,players,chanceCards,communityChestCards,currentPlayer)
    }

    override def saveGame(game: ControllerInterface): Unit = {
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
                case cell:Street => Json.obj(
                    "kind"->"Street",
                    "name"->cell.name,
                    "group"->cell.group,
                    "mortgage"->cell.mortgage,
                    "price"->cell.price,
                    "rent"->cell.rent,
                    "owner"->cell.owner,
                    "homecount"->cell.homecount,
                    "image"->cell.image
                )
                case cell:Eventcell => Json.obj(
                    "kind"->"Eventcell",
                    "name"->cell.name,
                    "group"->cell.group,
                    "image"->cell.image
                )
                case cell:CommunityChest => Json.obj(
                    "kind"->"CommunityChest",
                    "name"->cell.name,
                    "group"->cell.group,
                    "image"->cell.image
                )
                case cell:Los => Json.obj(
                    "kind"->"Go",
                    "name"->cell.name,
                    "group"->cell.group,
                    "image"->cell.image
                )
                case cell:IncomeTax => Json.obj(
                    "kind"->"IncomeTax",
                    "name"->cell.name,
                    "group"->cell.group,
                    "image"->cell.image
                )
                case cell:GoToJail => Json.obj(
                    "kind"->"GoToJail",
                    "name"->cell.name,
                    "group"->cell.group,
                    "image"->cell.image
                )
                case cell:Jail => Json.obj(
                    "kind"->"Jail",
                    "name"->cell.name,
                    "group"->cell.group,
                    "image"->cell.image
                )
                case cell:FreiParken=>Json.obj(
                    "kind"->"FreeParking",
                    "name"->cell.name,
                    "group"->cell.group,
                    "image"->cell.image
                )
                case cell:Zusatzsteuer=>Json.obj(
                    "kind"->"AdditionalTax",
                    "name"->cell.name,
                    "group"->cell.group,
                    "image"->cell.image
                )
            }
    }

    def gameToJson(game: ControllerInterface): JsObject = {
        Json.obj(
            "game" -> Json.obj(
                "chanceCards" -> Json.arr(
                    for {
                        i <- game.chanceCards.indices
                    } yield {
                        Json.obj(
                            "chancecard" -> Json.toJson(game.chanceCards(i))
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
            case "Go" => Los(name, group, image)
            case "Street" => Street(name, group, price, owner, rent, home, mortgage, image)
            case "CommunityChest" => CommunityChest(name, group,image)
            case "IncomeTax" => IncomeTax(name, group, image)
            case "Eventcell" => Eventcell(name, group,image)
            case "Jail" => Jail(name, group, image)
            case "FreeParking" => FreiParken(name, group, image)
            case "GoToJail" => GoToJail(name, group, image)
            case "AdditionalTax" => Zusatzsteuer(name, group, image)
            case _ => throw new UnsupportedOperationException
        }
    }
}