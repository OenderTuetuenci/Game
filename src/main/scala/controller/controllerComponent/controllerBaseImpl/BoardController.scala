package controller.controllerComponent.controllerBaseImpl

import model._
import scalafx.scene.image.Image

class BoardController(gameController: GameController) {

    def newOwner(playerNr: Int, cell: Cell): Cell = {
        val updated = cell.asInstanceOf[Buyable].setOwner(playerNr)
        updated
    }

    def createBoard : Vector[Cell] = {
        // group 0 go, etc, group 1 water,electricity, group 2 railroads,
        var board: Vector[Cell] = Vector()
        board = board :+ CellFactory("Los", "Go", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/Go.png"))
        board = board :+ CellFactory("Street", "Mediterranean Avenue", 3, 60, -1, 200, 0, mortgage = false, image = new Image("file:images/MediterraneanAve.png"))
        board = board :+ CellFactory("CommunityChest", "CommunityChest1", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/Go.png")) // todo
        board = board :+ CellFactory("Street", "Baltic Avenue", 3, 60, -1, 200, 0, mortgage = false, image = new Image("file:images/BalticAve.png"))
        board = board :+ CellFactory("IncomeTax", "IncomeTax", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/IncomeTax.png"))
        board = board :+ CellFactory("Street", "Reading Railroad", 2, 200, -1, 200, 0, mortgage = false, image = new Image("file:images/ReadingRailroad.png"))
        board = board :+ CellFactory("Street", "Oriental Avenue", 4, 100, -1, 200, 0, mortgage = false, image = new Image("file:images/OrientalAve.png"))
        board = board :+ CellFactory("Eventcell", "Eventcell1", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo
        board = board :+ CellFactory("Street", "Vermont Avenue", 4, 100, -1, 200, 0, mortgage = false, image = new Image("file:images/VermontAve.png"))
        board = board :+ CellFactory("Street", "Conneticut Avenue", 4, 120, -1, 200, 0, mortgage = false, image = new Image("file:images/ConneticutAve.png"))
        board = board :+ CellFactory("Jail", "Visit Jail", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/VisitJail.png"))

        board = board :+ CellFactory("Street", "St. Charles Place", 5, 140, -1, 200, 0, mortgage = false, image = new Image("file:images/StCharlesPlace.png"))
        board = board :+ CellFactory("Street", "Electric Company", 1, 150, -1, 200, 0, mortgage = false, image = new Image("file:images/ElectricCompany.png"))
        board = board :+ CellFactory("Street", "States Avenue", 5, 140, -1, 200, 0, mortgage = false, image = new Image("file:images/StatesAve.png"))
        board = board :+ CellFactory("Street", "Virgina Avenue", 5, 160, -1, 200, 0, mortgage = false, image = new Image("file:images/VirginiaAve.png"))
        board = board :+ CellFactory("Street", "Pennsylvania Railroad", 2, 200, -1, 200, 0, mortgage = false, image = new Image("file:images/PennsylvaniaRR.png"))
        board = board :+ CellFactory("Street", "St. James Place", 6, 180, -1, 200, 0, mortgage = false, image = new Image("file:images/StJamesPlace.png"))
        board = board :+ CellFactory("CommunityChest", "CommunityChest2", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo
        board = board :+ CellFactory("Street", "Tennessee Avenue", 6, 180, -1, 200, 0, mortgage = false, image = new Image("file:images/TennesseeAve.png"))
        board = board :+ CellFactory("Street", "New York Avenue", 6, 200, -1, 200, 0, mortgage = false, image = new Image("file:images/NewYorkAve.png"))
        board = board :+ CellFactory("FreeParking", "Free parking", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/ParkFree.png"))

        board = board :+ CellFactory("Street", "Kentucky Avenue", 7, 220, -1, 200, 0, mortgage = false, image = new Image("file:images/KentuckyAve.png"))
        board = board :+ CellFactory("Eventcell", "Eventcell2", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo
        board = board :+ CellFactory("Street", "Indiana Avenue", 7, 220, -1, 200, 0, mortgage = false, image = new Image("file:images/IndianaAve.png"))
        board = board :+ CellFactory("Street", "Illinois Avenue", 7, 240, -1, 200, 0, mortgage = false, image = new Image("file:images/IllinoisAve.png"))
        board = board :+ CellFactory("Street", "B & O Railroad", 2, 200, -1, 200, 0, mortgage = false, image = new Image("file:images/BnORailroad.png"))
        board = board :+ CellFactory("Street", "Atlantic Avenue", 8, 260, -1, 500, 0, mortgage = false, image = new Image("file:images/AtlanticAve.png"))
        board = board :+ CellFactory("Street", "Ventnor Avenue", 8, 260, -1, 800, 0, mortgage = false, image = new Image("file:images/VentnorAve.png"))
        board = board :+ CellFactory("Street", "Water Works", 1, 150, -1, 200, 0, mortgage = false, image = new Image("file:images/WaterWorks.png"))
        board = board :+ CellFactory("Street", "Marvin Gardens", 8, 280, -1, 2500, 0, mortgage = false, image = new Image("file:images/MarvinGardens.png"))
        board = board :+ CellFactory("GoToJail", "Go to jail", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/Jail.png"))

        board = board :+ CellFactory("Street", "Pacific Avenue", 9, 300, -1, 200, 0, mortgage = false, image = new Image("file:images/PacificAve.png"))
        board = board :+ CellFactory("Street", "North Carolina Avenue", 9, 300, -1, 200, 0, mortgage = false, image = new Image("file:images/NoCarolinaAve.png"))
        board = board :+ CellFactory("CommunityChest", "CommunityChest3", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo
        board = board :+ CellFactory("Street", "Pennsylvania Avenue", 9, 320, -1, 200, 0, mortgage = false, image = new Image("file:images/PennsylvaniaAve.png"))
        board = board :+ CellFactory("Street", "Short Line Railroad", 2, 200, -1, 200, 0, mortgage = false, image = new Image("file:images/ShortLineRR.png"))
        board = board :+ CellFactory("Eventcell", "Eventcell3", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo
        board = board :+ CellFactory("Street", "Park place", 10, 350, -1, 200, 0, mortgage = false, image = new Image("file:images/ParkPlace.png"))
        board = board :+ CellFactory("AdditionalTax", "Luxuary Tax", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/LuxuaryTax.png"))
        board = board :+ CellFactory("Street", "Broadwalk", 10, 400, -1, 200, 0, mortgage = false, image = new Image("file:images/Broadwalk.png"))
        board
        // todo create xml or json from board
        // then add xml/json parser
    }

    def activateStart(field: Los): Unit = {
        field.onPlayerEntered(gameController.currentPlayer)
        gameController.notifyObservers(OpenPlayerEnteredGoDialog(field))
    }


    def activateStreet(field: Buyable): Unit = {
        if (field.owner == -1 && field.owner != gameController.currentPlayer) gameController.notifyObservers(OpenBuyableFieldDialog(field))
        else gameController.notifyObservers(OpenPayRentDialog(field))

    }

    def activateIncomeTax(field: IncomeTax): Unit = {
        field.onPlayerEntered(gameController.currentPlayer)
        gameController.notifyObservers(OpenIncomeTaxDialog(field))
    }

    def activateVisitJail(field: Jail): Unit = {
        field.onPlayerEntered(gameController.currentPlayer)
        gameController.notifyObservers(OpenVisitJailDialog(field))
    }

    def activateFreiParken(field: FreiParken): Unit = {
        field.onPlayerEntered(gameController.currentPlayer)
        gameController.notifyObservers(OpenParkFreeDialog(field))

    }

    def activateLuxuaryTax(field: Zusatzsteuer): Unit = {
        field.onPlayerEntered(gameController.currentPlayer)
        gameController.notifyObservers(OpenLuxuaryTaxDialog(field))

    }

    def activateChance(field: Eventcell): Unit = {
        field.onPlayerEntered(gameController.currentPlayer)
        gameController.notifyObservers(OpenChanceDialog())
    }

    def activateCommunityChest(field: CommunityChest): Unit = {
        field.onPlayerEntered(gameController.currentPlayer)
        gameController.notifyObservers(OpenCommunityChestDialog())
    }

    def activateJail(field: GoToJail): Unit = {
        field.onPlayerEntered(gameController.currentPlayer)
        gameController.notifyObservers(openGoToJailDialog(field))
        gameController.players = gameController.players.updated(gameController.currentPlayer, gameController.players(gameController.currentPlayer).moveToJail)
        gameController.players = gameController.players.updated(gameController.currentPlayer, gameController.players(gameController.currentPlayer).incJailTime)
        gameController.notifyObservers(MovePlayerFigureEvent(-350, 350)) // jailxy
        val rollDiceBUtton = gameController.currentStage.scene().lookup("#rollDice") //.asInstanceOf[javafx.scene.control.Button]
        rollDiceBUtton.setDisable(true)
        val endTurnButton = gameController.currentStage.scene().lookup("#endTurn") //.asInstanceOf[javafx.scene.control.Button]
        endTurnButton.setDisable(false)
    }

    def buyHome(field: Street): (Vector[Cell], Vector[PlayerInterface]) = {
        val currentPlayer = gameController.currentPlayer
        var players = gameController.players
        var board = gameController.board
        players = players.updated(currentPlayer, players(currentPlayer).decMoney(200))
        board = board.updated(players(currentPlayer).position, board(players(currentPlayer).position).asInstanceOf[Street].buyHome(1))
        (board, players)
    }

    object CellFactory {
        def apply(kind: String, name: String, group: Int, price: Int, owner: Int, rent: Int, home: Int, mortgage: Boolean,
                  image: Image): Cell = kind match {
            case "Los" => Los(name, group, image: Image)
            case "Street" => Street(name, group, price, owner, rent, home, mortgage, image: Image)
            case "CommunityChest" => CommunityChest(name, group)
            case "IncomeTax" => IncomeTax(name, group, image: Image)
            case "Eventcell" => Eventcell(name, group)
            case "Jail" => Jail(name, group, image: Image)
            case "FreeParking" => FreiParken(name, group, image: Image)
            case "GoToJail" => GoToJail(name, group, image: Image)
            case "AdditionalTax" => Zusatzsteuer(name, group, image: Image)
            case _ => throw new UnsupportedOperationException
        }
    }
}

