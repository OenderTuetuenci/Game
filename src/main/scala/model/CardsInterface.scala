package model

trait CardsInterface {
    def shuffleChanceCards(cardList: List[String]): Vector[String]

    def shuffleCommunityChestCards(cardList: List[String]): Vector[String]

    def drawCard(deck: Vector[String]): Vector[String] = {
        deck.filterNot(elm => elm == deck.head)
    }
}