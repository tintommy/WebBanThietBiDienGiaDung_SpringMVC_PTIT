package ptithcm.dao;



import java.util.List;

import ptithcm.Entity.CTDonHangEntity;


public interface CTDonHangDAO{
	public void luuCtdh(CTDonHangEntity ctdh) ;
	public void updateCtdh(CTDonHangEntity ctdh) ;
	public List<CTDonHangEntity> timctdhTheoMaDh(int maDh);
    public CTDonHangEntity timCtdhTheoMaCtdh(int maCTDH);
    public List<CTDonHangEntity> layAllCTDonHang();	
}