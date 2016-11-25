package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.FotoDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCFotoDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Foto;
import at.ac.tuwien.sepm.ws16.qse01.service.FotoService;

/**
 * Created by macdnz on 24.11.16.
 */
public class FotoServiceImpl implements FotoService {
    private static JDBCFotoDAO dao;

    public FotoServiceImpl() throws Exception {
        dao = new JDBCFotoDAO();
    }

    @Override
    public Foto create(Foto f) {
        return dao.create(f);
    }

    @Override
    public Foto read(int id) {
        return dao.read(id);
    }

    @Override
    public String getLastImgPath(int shootingid) {
        return dao.getLastImgPath(shootingid);
    }

}
