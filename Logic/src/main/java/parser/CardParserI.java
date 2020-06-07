package parser;

import dto.Card;

public interface CardParserI {
    void reset();
    Card draw();
}
