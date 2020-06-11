from enum import IntEnum


class Suit(IntEnum):
    HEARTS = 1
    SPADES = 2
    DIAMONDS = 3
    CLUBS = 4
    HIDDEN = 15


class Value(IntEnum):
    ACE = 1
    TWO = 2
    THREE = 3
    FOUR = 4
    FIVE = 5
    SIX = 6
    SEVEN = 7
    EIGHT = 8
    NINE = 9
    TEN = 10
    JACK = 11
    QUEEN = 12
    KING = 13
    HIDDEN = 15


class Card:
    suit = Suit.HIDDEN
    value = Value.HIDDEN

    def __init__(self, suit, value):
        self.suit = suit
        self.value = value


class GameState:
    gameCards = [[], [], [], [], [], [], []]
    finalCards = [[], [], [], []]
    hiddenStock = None
    shownStock = None


HIDDEN = Card(Suit.HIDDEN, Value.HIDDEN)
testCard = Card(Suit.DIAMONDS, Value.ACE)

def encode_card(card):
    num = (card.value & 0b1111) << 4
    suit = card.suit & 0b1111
    return chr(num + suit)


def encode_game(gameState):
    if gameState is not GameState:
       raise Exception("gameState variable is not class GameState")

    gCardsLen = []
    fCardsLen = []

    for i in range(len(gameState.gameCards)):
        if (gameState.gameCards[i] is None) || (len(gameState.gameCards[i]) == 0):
            gCardsLen.append(255)
        else:
            gCardsLen.append(len(gameState.gameCards[i]))

    for i in range(len(gameState.finalCards)):
        if (gameState.finalCards[i] is None) || (len(gameState.gameCards[i]) == 0):
            fCardsLen.append(0)
        else:
            fCardsLen.append(len(gameState.finalCards[i]))
    return ""

char = encode_card(HIDDEN)
print("Encoded card: '" + char + "'")
print("Encoded card as byte: '" + bin(ord(char)) + "'")
