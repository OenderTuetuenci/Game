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
        var game: GameControllerInterface = null
        val source: String = Source.fromFile("game.json").getLines.mkString
        val json: JsValue = Json.parse(source)
        var chanceCards = Vector[String]()
        var communityChestCards = Vector[String]()
        val dataChanceCards = (json \ "game" \ "chanceCards").get.toString().replace("\"card\":","")
          .replace("[[","").replace("]]","").replace("\"","")
          .replace("{","").replace("}","").split(",")
        val dataCommunityChestCards = (json \ "game" \ "communityChestCards").get.toString().replace("\"card\":","")
          .replace("[[","").replace("]]","").replace("\"","")
          .replace("{","").replace("}","").split(",")
        dataChanceCards.foreach(c=>chanceCards=chanceCards:+c)
        dataCommunityChestCards.foreach(c=>communityChestCards=communityChestCards:+c)
        val humanplayer = (json \ "game" \"humanPlayers").get.toString().toInt
        val npcplayer = (json \ "game" \"npcPlayers").get.toString().toInt
        var board = Vector[Cell]()
        val dataBoard = (json \ "game" \ "board").get.toString().replace("\"card\":","")
          .replace("[","").replace("]","").replace("\"cell\":",";")
          .replace("{","").replace("\"","").replace("}","")
          .replace(",","").split(";")
        for(i<-1 until dataBoard.length){
            val t = dataBoard(i).replace("name:","").replace("group:",";")
              .replace("mortgage:",";").replace("price:",";").replace("rent:",";")
              .replace("owner:",";").replace("homecount:",";").replace("image:",";").split(";")
            t(0) match {
                case "Go" => board = board :+ CellFactory("Los", "Go", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/Go.png)")
                case "Mediterranean Avenue" =>board = board :+ CellFactory("Street", "Mediterranean Avenue", 3, 60,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/MediterraneanAve.png")
                case "CommunityChest1" =>board = board :+ CellFactory("CommunityChest", "CommunityChest1", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/Go.png") // todo
                case "Baltic Avenue" =>board = board :+ CellFactory("Street", "Baltic Avenue", 3, 60,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/BalticAve.png")
                case "IncomeTax" =>board = board :+ CellFactory("IncomeTax", "IncomeTax", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/IncomeTax.png")
                case "Reading Railroad" =>board = board :+ CellFactory("Street", "Reading Railroad", 2, 200,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/ReadingRailroad.png")
                case "Oriental Avenue" =>board = board :+ CellFactory("Street", "Oriental Avenue", 4, 100,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/OrientalAve.png")
                case "Eventcell1" =>board = board :+ CellFactory("Eventcell", "Eventcell1", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
                case "Vermont Avenue" =>board = board :+ CellFactory("Street", "Vermont Avenue", 4, 100,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/VermontAve.png")
                case "Conneticut Avenue" =>board = board :+ CellFactory("Street", "Conneticut Avenue", 4, 120,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/ConneticutAve.png")
                case "Visit Jail" =>board = board :+ CellFactory("Jail", "Visit Jail", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/VisitJail.png")

                case "St. Charles Place" =>board = board :+ CellFactory("Street", "St. Charles Place", 5, 140,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/StCharlesPlace.png")
                case "Electric Company" =>board = board :+ CellFactory("Street", "Electric Company", 1, 150,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/ElectricCompany.png")
                case "States Avenue" =>board = board :+ CellFactory("Street", "States Avenue", 5, 140,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/StatesAve.png")
                case "Virgina Avenue" =>board = board :+ CellFactory("Street", "Virgina Avenue", 5, 160,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/VirginiaAve.png")
                case "Pennsylvania Railroad" =>board = board :+ CellFactory("Street", "Pennsylvania Railroad", 2, 200,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/PennsylvaniaRR.png")
                case "St. James Place" =>board = board :+ CellFactory("Street", "St. James Place", 6, 180,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/StJamesPlace.png")
                case "CommunityChest2" =>board = board :+ CellFactory("CommunityChest", "CommunityChest2", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
                case "Tennessee Avenue" =>board = board :+ CellFactory("Street", "Tennessee Avenue", 6, 180,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/TennesseeAve.png")
                case "New York Avenue" =>board = board :+ CellFactory("Street", "New York Avenue", 6, 200,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/NewYorkAve.png")
                case "Free parking" =>board = board :+ CellFactory("FreeParking", "Free parking", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/ParkFree.png")

                case "Kentucky Avenue" =>board = board :+ CellFactory("Street", "Kentucky Avenue", 7, 220,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/KentuckyAve.png")
                case "Eventcell2" =>board = board :+ CellFactory("Eventcell", "Eventcell2", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
                case "Indiana Avenue" =>board = board :+ CellFactory("Street", "Indiana Avenue", 7, 220,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/IndianaAve.png")
                case "Illinois Avenue" =>board = board :+ CellFactory("Street", "Illinois Avenue", 7, 240,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/IllinoisAve.png")
                case "B & O Railroad" =>board = board :+ CellFactory("Street", "B & O Railroad", 2, 200,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/BnORailroad.png")
                case "Atlantic Avenue" =>board = board :+ CellFactory("Street", "Atlantic Avenue", 8, 260,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/AtlanticAve.png")
                case "Ventnor Avenue" =>board = board :+ CellFactory("Street", "Ventnor Avenue", 8, 260,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/VentnorAve.png")
                case "Water Works" =>board = board :+ CellFactory("Street", "Water Works", 1, 150, t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/WaterWorks.png")
                case "Marvin Gardens" =>board = board :+ CellFactory("Street", "Marvin Gardens", 8, 280,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/MarvinGardens.png")
                case "Go to jail" =>board = board :+ CellFactory("GoToJail", "Go to jail", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/Jail.png")

                case "Pacific Avenue" =>board = board :+ CellFactory("Street", "Pacific Avenue", 9, 300,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/PacificAve.png")
                case "North Carolina Avenue" =>board = board :+ CellFactory("Street", "North Carolina Avenue", 9, 300,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/NoCarolinaAve.png")
                case "CommunityChest3" =>board = board :+ CellFactory("CommunityChest", "CommunityChest3", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
                case "Pennsylvania Avenue" =>board = board :+ CellFactory("Street", "Pennsylvania Avenue", 9, 320,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/PennsylvaniaAve.png")
                case "Short Line Railroad" =>board = board :+ CellFactory("Street", "Short Line Railroad", 2, 200,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/ShortLineRR.png")
                case "Eventcell3" =>board = board :+ CellFactory("Eventcell", "Eventcell3", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
                case "Park place" =>board = board :+ CellFactory("Street", "Park place", 10, 350,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/ParkPlace.png")
                case "Luxuary Tax" =>board = board :+ CellFactory("AdditionalTax", "Luxuary Tax", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/LuxuaryTax.png")
                case "Broadwalk" =>board = board :+ CellFactory("Street", "Broadwalk", 10, 400,  t(5).toInt, t(4).toInt, t(6).toInt, mortgage = t(2).toBoolean, image = "file:images/Broadwalk.png")
                case _ =>
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
                            "card"->Json.toJson(game.chanceCards(i))
                        )
                    }
                ),
                "communityChestCards"-> Json.arr(
                    for{
                        i<-game.communityChestCards.indices
                    }yield {
                        Json.obj(
                            "card"->Json.toJson(game.communityChestCards(i))
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