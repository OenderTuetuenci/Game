package model.fileIOComponent.fileIOXmlImpl

import controller.controllerComponent.GameControllerInterface
import model.{Cell, CommunityChest, Eventcell, FreiParken, GoToJail, IncomeTax, Jail, Los, PlayerInterface, Street, Zusatzsteuer}
import model.fileIOComponent.FileIOInterface
import model.playerComponent.Player

import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOInterface {
    override def saveGame(game: GameControllerInterface):Unit = {
        import java.io._
        val pw = new PrintWriter(new File("game.xml"))
        val prettyPrinter = new PrettyPrinter(120, 4)
        val xml = prettyPrinter.format(gameToXml(game))
        pw.write(xml)
        pw.close
    }

    override def loadGame: (Int,Int,Int,Int,Int,Vector[Cell],Vector[PlayerInterface],Vector[String],Vector[String],Int) = {
        val file = scala.xml.XML.loadFile("game.xml")
        var chanceCards = Vector[String]()
        val chanceCardNodes = file\\"chanceCard"
        for(card <-chanceCardNodes){
            chanceCards = chanceCards :+ card.text.trim
        }
        var communityCards = Vector[String]()
        val communityNodes = (file\\"communityCard")
        for(card <-communityNodes){
            communityCards = communityCards :+ card.text.trim
        }
        val humanPlayers = (file\\"humanPlayers").text.trim.toInt
        val npcPlayers = (file\\"npcPlayers").text.trim.toInt
        var board = Vector[Cell]()
        val cellNodes = (file\\"cell")
        for(cell<-cellNodes){
            val kind = (cell\"@kind").text.trim
            val name = (cell\"name").text.trim
            val group = (cell\"group").text.trim.toInt
            var mortgage = false
            var price = 0
            var rent = 0
            var owner = -1
            var homecount = 0
            val image = (cell\"image").text.trim
            if(kind == "Street"){
                mortgage = (cell\"mortgage").text.trim.toBoolean
                price = (cell\"price").text.trim.toInt
                rent = (cell\"rent").text.trim.toInt
                owner = (cell\"owner").text.trim.toInt
                homecount = (cell\"homeCount").text.trim.toInt
            }
            board = board :+ CellFactory(kind,name,group,price,owner,rent,homecount,mortgage = mortgage,image)
        }
        var players = Vector[PlayerInterface]()
        val playerNodes = (file\\"player")
        for(player<-playerNodes){
            val name = (player \ "name").text.trim
            val position = (player \ "position").text.trim.toInt
            val money = (player \ "money").text.trim.toInt
            val jailCount = (player \ "jailCount").text.trim.toInt
            val turnPosition = (player \ "turnPosition").text.trim.toInt
            val rollForPosition = (player \ "rollForPosition").text.trim.toInt
            val figure = (player \ "figure").text.trim
            players = players :+ Player(name,position,money,jailCount,turnPosition,rollForPosition,figure)
        }
        val round = (file\\"round").text.trim.toInt
        val paschCount = (file \\ "paschCount").text.trim.toInt
        val collectedTax = (file\\ "collectedTax").text.trim.toInt
        val currentPlayer = (file\\ "currentPlayer").text.trim.toInt
        (humanPlayers,npcPlayers,round,paschCount,collectedTax,board,players,chanceCards,communityCards,currentPlayer)
    }
    def gameToXml(game: GameControllerInterface)={
        <game>
            <chanceCards length ={game.chanceCards.length.toString}>
                {
                    for{
                        i<-0 until game.chanceCards.length
                    }yield chanceCardToXml(game.chanceCards(i),i)
                }
            </chanceCards>
            <communityChestCards length ={game.communityChestCards.length.toString}>
                {
                for{
                    i<-0 until game.communityChestCards.length
                }yield communityCardToXml(game.communityChestCards(i),i)
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
    def chanceCardToXml(str: String,x:Int):Elem={
        <chanceCard number ={x.toString}>
            {str}
        </chanceCard>
    }
    def communityCardToXml(str: String,x:Int):Elem={
        <communityCard number ={x.toString}>
            {str}
        </communityCard>
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
            case cell:Los => <cell number ={x.toString} kind ={"Go"}>
                                    <name>{cell.name}</name>
                                    <group>{cell.group}</group>
                                    <image>{cell.image}</image>
                                </cell>
            case cell:CommunityChest => <cell number ={x.toString} kind ={"CommunityChest"}>
                <name>{cell.name}</name>
                <group>{cell.group}</group>
                <image>{cell.image}</image>
            </cell>
            case cell:IncomeTax => <cell number ={x.toString} kind ={"IncomeTax"}>
                <name>{cell.name}</name>
                <group>{cell.group}</group>
                <image>{cell.image}</image>
            </cell>
            case cell:Eventcell => <cell number ={x.toString} kind ={"Eventcell"}>
                <name>{cell.name}</name>
                <group>{cell.group}</group>
                <image>{cell.image}</image>
            </cell>
            case cell:Jail => <cell number ={x.toString} kind ={"Jail"}>
                <name>{cell.name}</name>
                <group>{cell.group}</group>
                <image>{cell.image}</image>
            </cell>
            case cell:FreiParken => <cell number ={x.toString} kind ={"FreeParking"}>
                <name>{cell.name}</name>
                <group>{cell.group}</group>
                <image>{cell.image}</image>
            </cell>
            case cell:GoToJail => <cell number ={x.toString} kind ={"GoToJail"}>
                <name>{cell.name}</name>
                <group>{cell.group}</group>
                <image>{cell.image}</image>
            </cell>
            case cell:Zusatzsteuer => <cell number ={x.toString} kind ={"AdditionalTax"}>
                <name>{cell.name}</name>
                <group>{cell.group}</group>
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
