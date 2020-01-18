package model.fileIOComponent.fileIOXmlImpl

import controller.controllerComponent.GameControllerInterface
import model.{Cell, PlayerInterface, Street}
import model.fileIOComponent.FileIOInterface

import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOInterface {
    override def saveGame(game: GameControllerInterface) = {
        import java.io._
        val pw = new PrintWriter(new File("game.xml"))
        val prettyPrinter = new PrettyPrinter(120, 4)
        val xml = prettyPrinter.format(gameToXml(game))
        pw.write(xml)
        pw.close
    }

    override def loadGame: (Int,Int,Int,Int,Int,Vector[Cell],Vector[PlayerInterface],Vector[String],Vector[String],Int) = {
        null
    }
    def gameToXml(game: GameControllerInterface)={
        <game>
            <chanceCards length ={game.chanceCards.length.toString}>
                {
                    for{
                        i<-0 until game.chanceCards.length
                    }yield cardToXml(game.chanceCards(i),i)
                }
            </chanceCards>
            <communityChestCards length ={game.communityChestCards.length.toString}>
                {
                for{
                    i<-0 until game.communityChestCards.length
                }yield cardToXml(game.communityChestCards(i),i)
                }
            </communityChestCards>
            <humanPlayers>{game.humanPlayers}</humanPlayers>
            <npcPlayers>{game.npcPlayers}</npcPlayers>
            <board length ={game.board.length.toString}>
                {
                for{
                    i<-0 until game.board.length
                }yield cellToXml(game.board(i),i)}
            </board>
            <players length ={game.players.length.toString}>
                {
                for{
                    i<-0 until game.players.length
                }yield playerToXml(game.players(i),i)
                }
            </players>
            <round>{game.round}</round>
            <paschCount>{game.paschCount}</paschCount>
            <collectedTax>{game.collectedTax}</collectedTax>
            <currentPlayer>{game.currentPlayer}</currentPlayer>
        </game>
    }
    def cardToXml(str: String,x:Int):Elem={
        <card number ={x.toString}>
            {str}
        </card>
    }
    def cellToXml(cell: Cell, x: Int):Elem={
        cell match {
            case cell:Street => <cell number ={x.toString} kind ={"Street"}>
                                    <name>{cell.name}</name>
                                    <group>{cell.group}</group>
                                    <mortgage>{cell.mortgage}</mortgage>
                                    <price>{cell.price}</price>
                                    <rent>{cell.rent}</rent>
                                    <owner>{cell.owner}</owner>
                                    <homeCount>{cell.homecount}</homeCount>
                                    <image>{cell.image}</image>
                                </cell>
            case cell:Cell => <cell number ={x.toString} kind ={"Cell"}>
                                    <name>{cell.name}</name>
                                    <name>{cell.group}</name>
                                    <image>{cell.image}</image>
                                </cell>
        }

    }
    def playerToXml(player: PlayerInterface, i: Int): Elem={
        <player number ={i.toString}>
            <name>{player.name}</name>
            <position>{player.position}</position>
            <money>{player.money}</money>
            <jailCount>{player.jailCount}</jailCount>
            <turnPosition>{player.turnPosition}</turnPosition>
            <rollForPosition>{player.rollForPosition}</rollForPosition>
            <figure>{player.figure}</figure>
        </player>
    }

}
