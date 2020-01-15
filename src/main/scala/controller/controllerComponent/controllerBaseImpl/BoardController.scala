package controller.controllerComponent.controllerBaseImpl

import model._

class BoardController(gameController: GameController) {

    def newOwner(playerNr: Int, cell: Cell): Cell = {
        val updated = cell.asInstanceOf[Buyable].setOwner(playerNr)
        updated
    }

    def createBoard : Vector[Cell] = {
        // group 0 go, etc, group 1 water,electricity, group 2 railroads,
        var board: Vector[Cell] = Vector()
        board = board :+ CellFactory("Go", "Go", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/Go.png")
        board = board :+ CellFactory("Street", "Mediterranean Avenue", 3, 60, -1, 200, 0, mortgage = false, image = "file:images/MediterraneanAve.png")
        board = board :+ CellFactory("CommunityChest", "CommunityChest1", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/Go.png") // todo
        board = board :+ CellFactory("Street", "Baltic Avenue", 3, 60, -1, 200, 0, mortgage = false, image = "file:images/BalticAve.png")
        board = board :+ CellFactory("IncomeTax", "IncomeTax", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/IncomeTax.png")
        board = board :+ CellFactory("Street", "Reading Railroad", 2, 200, -1, 200, 0, mortgage = false, image = "file:images/ReadingRailroad.png")
        board = board :+ CellFactory("Street", "Oriental Avenue", 4, 100, -1, 200, 0, mortgage = false, image = "file:images/OrientalAve.png")
        board = board :+ CellFactory("Eventcell", "Eventcell1", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
        board = board :+ CellFactory("Street", "Vermont Avenue", 4, 100, -1, 200, 0, mortgage = false, image = "file:images/VermontAve.png")
        board = board :+ CellFactory("Street", "Conneticut Avenue", 4, 120, -1, 200, 0, mortgage = false, image = "file:images/ConneticutAve.png")
        board = board :+ CellFactory("Jail", "Visit Jail", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/VisitJail.png")

        board = board :+ CellFactory("Street", "St. Charles Place", 5, 140, -1, 200, 0, mortgage = false, image = "file:images/StCharlesPlace.png")
        board = board :+ CellFactory("Street", "Electric Company", 1, 150, -1, 200, 0, mortgage = false, image = "file:images/ElectricCompany.png")
        board = board :+ CellFactory("Street", "States Avenue", 5, 140, -1, 200, 0, mortgage = false, image = "file:images/StatesAve.png")
        board = board :+ CellFactory("Street", "Virgina Avenue", 5, 160, -1, 200, 0, mortgage = false, image = "file:images/VirginiaAve.png")
        board = board :+ CellFactory("Street", "Pennsylvania Railroad", 2, 200, -1, 200, 0, mortgage = false, image = "file:images/PennsylvaniaRR.png")
        board = board :+ CellFactory("Street", "St. James Place", 6, 180, -1, 200, 0, mortgage = false, image = "file:images/StJamesPlace.png")
        board = board :+ CellFactory("CommunityChest", "CommunityChest2", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
        board = board :+ CellFactory("Street", "Tennessee Avenue", 6, 180, -1, 200, 0, mortgage = false, image = "file:images/TennesseeAve.png")
        board = board :+ CellFactory("Street", "New York Avenue", 6, 200, -1, 200, 0, mortgage = false, image = "file:images/NewYorkAve.png")
        board = board :+ CellFactory("FreeParking", "Free parking", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/ParkFree.png")

        board = board :+ CellFactory("Street", "Kentucky Avenue", 7, 220, -1, 200, 0, mortgage = false, image = "file:images/KentuckyAve.png")
        board = board :+ CellFactory("Eventcell", "Eventcell2", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
        board = board :+ CellFactory("Street", "Indiana Avenue", 7, 220, -1, 200, 0, mortgage = false, image = "file:images/IndianaAve.png")
        board = board :+ CellFactory("Street", "Illinois Avenue", 7, 240, -1, 200, 0, mortgage = false, image = "file:images/IllinoisAve.png")
        board = board :+ CellFactory("Street", "B & O Railroad", 2, 200, -1, 200, 0, mortgage = false, image = "file:images/BnORailroad.png")
        board = board :+ CellFactory("Street", "Atlantic Avenue", 8, 260, -1, 500, 0, mortgage = false, image = "file:images/AtlanticAve.png")
        board = board :+ CellFactory("Street", "Ventnor Avenue", 8, 260, -1, 800, 0, mortgage = false, image = "file:images/VentnorAve.png")
        board = board :+ CellFactory("Street", "Water Works", 1, 150, -1, 200, 0, mortgage = false, image = "file:images/WaterWorks.png")
        board = board :+ CellFactory("Street", "Marvin Gardens", 8, 280, -1, 2500, 0, mortgage = false, image = "file:images/MarvinGardens.png")
        board = board :+ CellFactory("GoToJail", "Go to jail", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/Jail.png")

        board = board :+ CellFactory("Street", "Pacific Avenue", 9, 300, -1, 200, 0, mortgage = false, image = "file:images/PacificAve.png")
        board = board :+ CellFactory("Street", "North Carolina Avenue", 9, 300, -1, 200, 0, mortgage = false, image = "file:images/NoCarolinaAve.png")
        board = board :+ CellFactory("CommunityChest", "CommunityChest3", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
        board = board :+ CellFactory("Street", "Pennsylvania Avenue", 9, 320, -1, 200, 0, mortgage = false, image = "file:images/PennsylvaniaAve.png")
        board = board :+ CellFactory("Street", "Short Line Railroad", 2, 200, -1, 200, 0, mortgage = false, image = "file:images/ShortLineRR.png")
        board = board :+ CellFactory("Eventcell", "Eventcell3", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
        board = board :+ CellFactory("Street", "Park place", 10, 350, -1, 200, 0, mortgage = false, image = "file:images/ParkPlace.png")
        board = board :+ CellFactory("AdditionalTax", "Luxuary Tax", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/LuxuaryTax.png")
        board = board :+ CellFactory("Street", "Broadwalk", 10, 400, -1, 200, 0, mortgage = false, image = "file:images/Broadwalk.png")
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
                  image: String): Cell = kind match {
            case "Go" => Los(name, group, image: String)
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

