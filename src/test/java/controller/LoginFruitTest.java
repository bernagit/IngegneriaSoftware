package controller;

import model.user.Fruitore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class LoginFruitTest {
    LoginFruit login = new LoginFruit();
    Fruitore fruitore;
    InputStream systemIn;
    @Test
    void dofirstLogin() {
        File file = new File("./src/test/cases/FirstLoginFruit");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);
        fruitore = (Fruitore) login.doLogin();

        assertNotEquals(fruitore, null);
        assertEquals(fruitore.getUsername(), "niccolo");
        assertFalse(fruitore.getUserType());
    }

}