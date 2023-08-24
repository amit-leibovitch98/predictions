package file;

import file.schema.generated.PRDWorld;
import simulation.world.World;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FIleHandler {
    private PRDWorld prdWorld;
    private InputStream inputStream;
    private final String JAXB_XML_PACKAGE_NAME = "file.schema.generated";

    public FIleHandler(String path) {
        try {
            inputStream = new FileInputStream(path);
            this.prdWorld = readWorld();
        } catch (JAXBException | FileNotFoundException e) {
            throw new IllegalArgumentException("File not found");
        }
    }

    public PRDWorld getPrdWorld() {
        return prdWorld;
    }

    private PRDWorld readWorld() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (PRDWorld) unmarshaller.unmarshal(inputStream);
    }
}
