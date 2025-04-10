package oogasalad.model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.MissingResourceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ResourceBundlesTest {

    private static final String VALID_BUNDLE = "bundles.test";
    private static final String INVALID_BUNDLE = "invalidBundle";
    private static final String VALID_KEY = "windowX";
    private static final String INVALID_KEY = "invalidKey";
    private static final String VALID_VALUE = "0";

    @BeforeEach
    public void setup() {
        ResourceBundles.loadBundle(VALID_BUNDLE);
        ResourceBundles.setActiveBundle(VALID_BUNDLE);
    }

    @Test
    public void loadBundle_testLoadValidBundle_returnsSuccesfully() {
        assertDoesNotThrow(() -> ResourceBundles.loadBundle(VALID_BUNDLE));
    }

    @Test
    public void loadBundle_testLoadInvalidBundle_throwsMissingResourceException() {
        assertThrows(MissingResourceException.class, () -> ResourceBundles.loadBundle(INVALID_BUNDLE));
    }

    @Test
    public void setActiveBundle_setAndRetrieve_returnsTrue() {
        ResourceBundles.setActiveBundle(VALID_BUNDLE);
        assertEquals(VALID_VALUE, ResourceBundles.getString(VALID_KEY));
    }

    @Test
    public void setActiveBundle_setInvalidBundle_throwsException() {
        assertDoesNotThrow(() -> ResourceBundles.setActiveBundle(INVALID_BUNDLE));
    }

    @Test
    public void getString_getValuePassingInBundle_returnsTrue() {
        String value = ResourceBundles.getString(VALID_BUNDLE, VALID_KEY);
        assertEquals(VALID_VALUE, value);
    }

    @Test
    public void getString_getInvalidKey_assertsNull() {
        String value = ResourceBundles.getString(VALID_BUNDLE, INVALID_KEY);
        assertNull(value);
    }

    @Test
    public void getInt_loadValidInt_assertsEqual() {
        int value = ResourceBundles.getInt(VALID_BUNDLE, "size");
        assertEquals(500, value);
    }

    @Test
    public void getInt_invalidKey_throwsException() {
        int value = ResourceBundles.getInt(VALID_BUNDLE, INVALID_KEY);
        assertEquals(0, value);
    }

    @Test
    public void getDouble_validDouble_assertsEqual() {
        double value = ResourceBundles.getDouble(VALID_BUNDLE, "testDouble");
        assertEquals(3.14, value, 0.001);
    }

    @Test
    public void getDouble_invalidKey_returnsZero() {
        double value = ResourceBundles.getDouble(VALID_BUNDLE, INVALID_KEY);
        assertEquals(0.0, value, 0.001);
    }

    @Test
    public void getBoolean_validKey_assertsTrue() {
        boolean value = ResourceBundles.getBoolean(VALID_BUNDLE, "testBoolean");
        assertTrue(value);
    }

    @Test
    public void getBoolean_invalidKey_returnsTrue() {
        boolean value = ResourceBundles.getBoolean(VALID_BUNDLE, INVALID_KEY);
        assertFalse(value);
    }
}
