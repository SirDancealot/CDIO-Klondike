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
    hiddenStock = None
    shownStock = None


HIDDEN = Card(Suit.HIDDEN, Value.HIDDEN)
testCard = Card(Suit.DIAMONDS, Value.ACE)


def encode_card(card):
    if card is None:
        return chr(0b0)
    print(card.value)
    print(card.suit)
    num = (card.value & 0b1111) << 4
    suit = card.suit & 0b1111
    return chr(num + suit)


def encode_game(gameState):
    if gameState.__class__ is not GameState:
        raise Exception("gameState variable is not class GameState")

    return_str = ""
    gCardsLen = []

    for i in range(len(gameState.gameCards)):
        if (gameState.gameCards[i] is None) or (len(gameState.gameCards[i]) == 0):
            gCardsLen.append(0)
        else:
            gCardsLen.append(len(gameState.gameCards[i]))

    for i in range(len(gCardsLen)):
        return_str += chr(gCardsLen[i])
        for j in range(gCardsLen[i]):
            return_str += encode_card(gameState.gameCards[i][j])

    for i in range(4):
        return_str += encode_card(gameState.finalCards[i])

    if gameState.hiddenStock is None:
        return_str += chr(0xFF)
    else:
        return_str += encode_card(gameState.hiddenStock)

    if gameState.shownStock is None:
        return_str += chr(0xFF)
    else:
        return_str += encode_card(gameState.shownStock)

    return return_str

state = GameState()
#state.shownStock = Card(Suit.DIAMONDS, Value.FIVE)
#state.finalCards[0] = Card(Suit.DIAMONDS, Value.ACE)
#state.finalCards[1] = Card(Suit.CLUBS, Value.ACE)
#state.finalCards[2] = Card(Suit.SPADES, Value.ACE)
#state.finalCards[3] = Card(Suit.HEARTS, Value.ACE)
#state.gameCards[0].append(Card(Suit.HEARTS, Value.KING))
#state.gameCards[0].append(Card(Suit.CLUBS, Value.QUEEN))
#state.gameCards[1].append(Card(Suit.DIAMONDS, Value.TEN))
#state.gameCards[2].append(Card(Suit.SPADES, Value.TWO))
#state.gameCards[3].append(Card(Suit.CLUBS, Value.FIVE))
#state.gameCards[4].append(Card(Suit.HEARTS, Value.FIVE))
#state.gameCards[5].append(Card(Suit.SPADES, Value.TEN))
#state.gameCards[6].append(Card(Suit.HEARTS, Value.EIGHT))

char = encode_card(HIDDEN)
test = encode_game(state)
print("Encoded card: '" + char + "'")
print("Encoded card as byte: '" + bin(ord(char)) + "'")
print("test: '" + test + "'")

for c in test:
    print(bin(ord(c)))
