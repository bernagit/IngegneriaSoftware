package controller;

import model.user.Configuratore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class LoginConfTest {
    LoginConf login = new LoginConf();

    Configuratore configuratore;

    InputStream systemIn;
    @Test
    void doLogin() {
        File file = new File("./src/test/cases/LoginConf");
        try {
            systemIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
        }
        System.setIn(systemIn);
        configuratore = login.doLogin();
        //test
        assertEquals(configuratore.getUsername(), "gianni");
        assertFalse(configuratore.getFirstLogin());
        assertTrue(configuratore.getUserType());
    }
}