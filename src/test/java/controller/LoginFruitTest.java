package controller;

import model.user.Fruitore;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class LoginFruitTest {
    LoginFruit login = new LoginFruit();
    Fruitore fruitore;
    InputStream systemIn;

    ByteArrayOutputStream systemOut = new ByteArrayOutputStream();
    @Test
    void dofirstLogin() {
        File file = new File("./src/test/cases/LoginFruit");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);
        System.setOut(new PrintStream(systemOut));

        fruitore = (Fruitore) login.doLogin();

        assertEquals("Primo Login? (S/N): Inserisci username: Inserisci password: ", systemOut.toString());
        assertNotEquals(fruitore, null);
        assertEquals(fruitore.getUsername(), "niccolo");
        assertFalse(fruitore.getUserType());

    }

}