package kadra.mapki.styl;

/**
 * Określa styl węzła.
 * <p>
 * Obecnie klasa ma tylko jeden atrybut, więc osobna klasa jest trochę na wyrost. Natomiast w
 * przyszłości (nie w tym zadaniu) mogłoby się pojawić tutaj więcej aspektów stylu. Dzięki osobnej
 * klasie moglibyśmy dodawać nowe aspekty stylu bez modyfikacji interfejsu (w znaczeniu "sposób
 * korzystania z klasy" a nie Javowego interfejsu) klasy GeneratorMapek.
 * <p>
 * Notabene, ta klasa mogłaby być rekordem
 * (<a href="https://docs.oracle.com/en/java/javase/21/language/records.html">dokumentacja</a>),
 * ale dla uproszczenia (nie omawialiśmy rekordów na laboratorium) tworzymy zwykłą klasę.
 */
public record StylWezla(GruboscKonturu gruboscKonturu) {
    public StylWezla {
        if (gruboscKonturu == null) {
            throw new IllegalArgumentException("Grubość konturu nie może być nullem.");
        }

    }

    @Override
    public String toString() {
        return "StylWezla{gruboscKonturu=%s}".formatted(gruboscKonturu);
    }
}
