import at.ac.tuwien.sepm.ws16.qse01.dao.FotoDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCFotoDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Foto;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by macdnz on 25.11.16.
 */
public class FotoDatoTest {
    private FotoDAO dao;

    @Before
    public void before() throws Exception {
        dao = new JDBCFotoDAO();
    }

    /**
     * This test creates a null-image.
     * DAO should throw IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void createNullImage() throws Throwable  {
        dao.create(null);
    }

    /**
     * This test creates an image with valid data.
     * DAO should save it in database.
     */
    @Test
    public void createValidImage() throws Throwable  {
        Foto img = new Foto(99,"/images/asdfas.jpg",2,new Date());
        img.setAutoDate();

        //There is no image in database with ID = 99
        assertThat(String.valueOf(dao.read(99)),is("null"));

        Foto createdImg = dao.create(img);

        //Check if the created image saved.
        assertThat(dao.read(createdImg.getImageID()).getImagepath(),is(img.getImagepath()));
    }





}
