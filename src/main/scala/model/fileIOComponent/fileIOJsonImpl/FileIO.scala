package model.fileIOComponent.fileIOJsonImpl

import controller.controllerComponent.GameControllerInterface
import model.{Buyable, Cell, PlayerInterface}
import model.fileIOComponent.FileIOInterface
import model.playerComponent.Player
import play.api.libs.json.{JsNumber, JsObject, JsValue, Json, Writes}

class FileIO extends FileIOInterface {

    override def loadGame: Unit = {
        ;
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
                "collectedTax"->JsNumber(game.collectedTax)
            )
        )
    }
}