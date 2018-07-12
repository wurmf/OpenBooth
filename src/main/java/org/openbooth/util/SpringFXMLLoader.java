package org.openbooth.util;

import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * A Spring Based FXMLLoader.
 *
 * Provides the possibility to load FXML-Files and wrap them for convenient access to the loaded class
 * as well as the spring loaded controller.
 *
 * @author Dominik Moser
 */
@Component
public class SpringFXMLLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringFXMLLoader.class);

    private ApplicationContext applicationContext;

    @Autowired
    public SpringFXMLLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private FXMLLoader getFXMLLoader() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        return fxmlLoader;
    }

    /**
     * Load and return an object from an FXML-File.
     *
     * @param inputStream the input stream of the FXML-File
     * @param loadType the class of the object to load
     * @param <T> the loaded object
     * @return the loaded object
     * @throws IOException if the resource could not be loaded
     */
    public synchronized <T> T load(InputStream inputStream, Class<T> loadType) throws IOException {
        LOGGER.trace("Loading object of type {} from fxml resource {}", loadType.getCanonicalName(), inputStream);
        return this.getFXMLLoader().load(inputStream);
    }

    /**
     * Load and return an object from an FXML-File.
     *
     * @param pathToFXMLFile path to the FXML-File
     * @param loadType the class of the object to load
     * @param <T> the loaded object
     * @return the loaded object
     * @throws IOException if the resource could not be loaded
     */
    public <T> T load(String pathToFXMLFile, Class<T> loadType) throws IOException {
        return this.load(SpringFXMLLoader.class.getResourceAsStream(pathToFXMLFile), loadType);
    }

    /**
     * Load and return an object from an FXML-File.
     *
     * @param inputStream the input stream of the FXML-File
     * @return the loaded object
     * @throws IOException if the resource could not be loaded
     */
    public Object load(InputStream inputStream) throws IOException {
        return this.load(inputStream, Object.class);
    }

    /**
     * Load and return an object from an FXML-File.
     *
     * @param pathToFXMLFile path to the FXML-File
     * @return the loaded object
     * @throws IOException if the resource could not be loaded
     */
    public Object load(String pathToFXMLFile) throws IOException {
        return this.load(SpringFXMLLoader.class.getResourceAsStream(pathToFXMLFile));
    }

    /**
     * Load and wrap an object and the declared controller from an FXML-File.
     *
     * @param inputStream the input stream of the FXML-File
     * @param loadType the class of the object to load
     * @param controllerType the class of the declared controller of the loaded object
     * @param <T1> the loaded object
     * @param <T2> the controller of the loaded object
     * @return a wrapper object containing the loaded object and its declared controller
     * @see FXMLWrapper
     * @throws IOException if the resource could not be loaded
     */
    public synchronized <T1, T2> FXMLWrapper<T1, T2> loadAndWrap(
            InputStream inputStream,
            Class<T1> loadType,
            Class<T2> controllerType) throws IOException {
        FXMLLoader fxmlLoader = this.getFXMLLoader();
        LOGGER.trace("Loading and wrapping object of type {} with controller of type {} from fxml resource {}",
                loadType.getCanonicalName(), controllerType.getCanonicalName(), inputStream);
        return new FXMLWrapper<>(
                fxmlLoader.load(inputStream),
                fxmlLoader.getController());
    }

    /**
     * Load and wrap an object and the declared controller from an FXML-File.
     *
     * @param pathToFXMLFile path to the FXML-File
     * @param loadType the class of the object to load
     * @param controllerType the class of the declared controller of the loaded object
     * @param <T1> the loaded object
     * @param <T2> the controller of the loaded object
     * @return a wrapper object containing the loaded object and its declared controller
     * @see FXMLWrapper
     * @throws IOException if the resource could not be loaded
     */
    public synchronized <T1, T2> FXMLWrapper<T1, T2> loadAndWrap(
            String pathToFXMLFile,
            Class<T1> loadType,
            Class<T2> controllerType) throws IOException {
        return this.loadAndWrap(SpringFXMLLoader.class.getResourceAsStream(pathToFXMLFile), loadType, controllerType);
    }

    /**
     * Load and wrap an object and the declared controller from an FXML-File.
     *
     * @param inputStream the input stream of the FXML-File
     * @param controllerType the class of the declared controller of the loaded object
     * @param <T2> the controller of the loaded object
     * @return a wrapper object containing the loaded object and its declared controller
     * @see FXMLWrapper
     * @throws IOException if the resource could not be loaded
     */
    public <T2> FXMLWrapper<Object, T2> loadAndWrap(InputStream inputStream, Class<T2> controllerType) throws IOException {
        return this.loadAndWrap(inputStream, Object.class, controllerType);
    }

    /**
     * Load and wrap an object and the declared controller from an FXML-File.
     *
     * @param pathToFXMLFile path to the FXML-File
     * @param controllerType the class of the declared controller of the loaded object
     * @param <T2> the controller of the loaded object
     * @return a wrapper object containing the loaded object and its declared controller
     * @see FXMLWrapper
     * @throws IOException if the resource could not be loaded
     */
    public <T2> FXMLWrapper<Object, T2> loadAndWrap(String pathToFXMLFile, Class<T2> controllerType) throws IOException {
        return this.loadAndWrap(pathToFXMLFile, Object.class, controllerType);
    }

    /**
     * Load and wrap an object and the declared controller from an FXML-File.
     *
     * @param inputStream the input stream of the FXML-File
     * @return a wrapper object containing the loaded object and its declared controller
     * @see FXMLWrapper
     * @throws IOException if the resource could not be loaded
     */
    public FXMLWrapper<Object, Object> loadAndWrap(InputStream inputStream) throws IOException {
        return this.loadAndWrap(inputStream, Object.class, Object.class);
    }

    /**
     * Load and wrap an object and the declared controller from an FXML-File.
     *
     * @param pathToFXMLFile path to the FXML-File
     * @return a wrapper object containing the loaded object and its declared controller
     * @see FXMLWrapper
     * @throws IOException if the resource could not be loaded
     */
    public FXMLWrapper<Object, Object> loadAndWrap(String pathToFXMLFile) throws IOException {
        return this.loadAndWrap(pathToFXMLFile, Object.class, Object.class);
    }

    /**
     * A wrapper for loading FXML-files and wrapping the loaded object as well as the controller.
     *
     * @param <T1> the loaded object
     * @param <T2> the controller of the loaded object
     */
    public class FXMLWrapper<T1, T2> {

         public T1 getLoadedObject() {
            return loadedObject;
        }

        public T2 getController() {
            return controller;
        }

        private final T1 loadedObject;
        private final T2 controller;

        private FXMLWrapper(T1 loadedObject, T2 controller) {
            this.loadedObject = loadedObject;
            this.controller = controller;
        }
    }
}
