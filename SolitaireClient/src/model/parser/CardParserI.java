package model.parser;

import model.dto.Card;

public interface CardParserI {
    void reset();
    Card draw();
}
