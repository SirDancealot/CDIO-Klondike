from enum import IntEnum


class Suit(IntEnum):
    HIDDEN = 0
    HEARTS = 1
    SPADES = 2
    DIAMONDS = 3
    CLUBS = 4


class Value(IntEnum):
    HIDDEN = 0
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


class Card:
    suit = Suit.HIDDEN
    value = Value.HIDDEN

    def __init__(self, suit, value):
        self.suit = suit
        self.value = value


class GameState:
    gameCards = [[], [], [], [], [], [], []]
    finalCards = [None, None, None, None]
    shownStock = None


HIDDEN = Card(Suit.HIDDEN, Value.HIDDEN)
testCard = Card(Suit.DIAMONDS, Value.ACE)


def encode_card(card):
    if card is None:
        return chr(0b0)
    num = (card.value & 0b1111) << 4
    suit = card.suit & 0b1111
    return num + suit


def encode_game(gameState):
    if gameState.__class__ is not GameState:
        raise Exception("gameState variable is not class GameState")

    byte_arr = bytearray()

    return_str = ""
    gCardsLen = []

    for i in range(len(gameState.gameCards)):
        if (gameState.gameCards[i] is None) or (len(gameState.gameCards[i]) == 0):
            gCardsLen.append(0)
        else:
            gCardsLen.append(len(gameState.gameCards[i]))

    for i in range(len(gCardsLen)):
        # return_str += chr(gCardsLen[i])
        byte_arr.append(gCardsLen[i])
        for j in range(gCardsLen[i]):
            # return_str += encode_card(gameState.gameCards[i][j])
            byte_arr.append(encode_card(gameState.gameCards[i][j]))

    for i in range(4):
        # return_str += encode_card(gameState.finalCards[i])
        byte_arr.append(encode_card(gameState.finalCards[i]))

    if gameState.shownStock is None:
        # return_str += chr(0xFF)
        byte_arr.append(0x00)
    else:
        # return_str += encode_card(gameState.shownStock)
        byte_arr.append(encode_card(gameState.shownStock))

    byte_arr.append(0xFF)

    for byte in byte_arr:
        print(bin(byte))

    return byte_arr
