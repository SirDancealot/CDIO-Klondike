/**
 * @author Michael Jeppesen s185123
 */

package model.parser;

import model.dto.Card;

public interface CardParserI {
    void reset();
    Card draw();
}
