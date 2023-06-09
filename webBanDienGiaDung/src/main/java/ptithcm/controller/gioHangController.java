package ptithcm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ptithcm.Entity.GioHangEntity;
import ptithcm.Entity.LoaiSanPhamEntity;
import ptithcm.Entity.NguoiDungEntity;
import ptithcm.Entity.SanPhamEntity;
import ptithcm.service.gioHangService;

@Transactional
@Controller

public class gioHangController {
	@Autowired
	SessionFactory factory;

	@Autowired
	gioHangService gioHangService;

	
	@RequestMapping("gioHang")
	public String gioHang(HttpServletRequest request, ModelMap model) {

		HttpSession session0 = request.getSession();

		NguoiDungEntity user = (NguoiDungEntity) session0.getAttribute("USER");

		if (user == null) {
			model.addAttribute("user", new NguoiDungEntity());

			return "/user/login";
		}

		Session session = factory.getCurrentSession();
		String hql = "FROM GioHangEntity where nguoiDung.maNd=:maNd";

		Query query = session.createQuery(hql);
		query.setParameter("maNd", user.getMaNd());
		List<GioHangEntity> gioHangList = query.list();

		model.addAttribute("gioHangList", gioHangList);

		return "/gioHang/gioHang";
	}
	
	
	@RequestMapping(value = "/gioHang/{maGh}", params = "update", method = RequestMethod.POST)
	public String update(@PathVariable("maGh") int maGh, HttpServletRequest request, ModelMap model) {

		HttpSession session0 = request.getSession();
		NguoiDungEntity user = (NguoiDungEntity) session0.getAttribute("USER");

		int soLuong = Integer.parseInt(request.getParameter("soLuong"));

		gioHangService.updateSoLuong(soLuong, maGh);

		List<GioHangEntity> gioHangList = gioHangService.layGioHangCuaUser(user.getMaNd());

		model.addAttribute("gioHangList", gioHangList);

		return "/gioHang/gioHang";
	}

	@RequestMapping(value = "/gioHang/{maGh}", params = "xoa", method = RequestMethod.POST)
	public String xoa(@PathVariable("maGh") int maGh, HttpServletRequest request, ModelMap model) {
		HttpSession session0 = request.getSession();
		NguoiDungEntity user = (NguoiDungEntity) session0.getAttribute("USER");

		gioHangService.deleteGioHang(maGh);

		List<GioHangEntity> gioHangList = gioHangService.layGioHangCuaUser(user.getMaNd());

		model.addAttribute("gioHangList", gioHangList);

		return "/gioHang/gioHang";

	}

	@RequestMapping(value = "/themVaoGio/{maSP}", params = "them", method = RequestMethod.POST)
	public String them(@PathVariable("maSP") String maSP, HttpServletRequest request, ModelMap model) {

		HttpSession session0 = request.getSession();
		NguoiDungEntity user = (NguoiDungEntity) session0.getAttribute("USER");
	
		SanPhamEntity sanPham = laySanPhamTheoMa(maSP);
		
		if(user==null)
		{
			model.addAttribute("user", new NguoiDungEntity());
		
			session0.setAttribute("SANPHAM", maSP);
			
			return "/user/login";
		}
		
			
		GioHangEntity gioHang = gioHangService.layGioHangTheoMaNdVaSanPham(user.getMaNd(), maSP);
		int soLuong = Integer.parseInt(request.getParameter("soLuong"));

		if (gioHang == null) {
			gioHang = new GioHangEntity();

			gioHang.setNguoiDung(user);
			gioHang.setSanPham(sanPham);
			gioHang.setSoLuong(soLuong);

			gioHangService.addGioHang(gioHang);

			String mes = "Thêm " + String.valueOf(soLuong) + " sản phẩm vào giỏ hàng thành công !";
			model.addAttribute("messenger", mes);
		}

		else {
			gioHang.setSoLuong(gioHang.getSoLuong() + soLuong);

			gioHangService.updateGioHang(gioHang);
			String mes = "Thêm " + String.valueOf(soLuong) + " sản phẩm vào giỏ hàng thành công !";

			model.addAttribute("messenger", mes);
		}

		model.addAttribute("sanPham", sanPham);
		List<SanPhamEntity> sanPhamCungLoai = laySanPhamCungLoai(sanPham.getLoaiSanPham(), maSP);
		model.addAttribute("sanPhamCungLoai", sanPhamCungLoai);
		return "/sanPham/sanPham";

	}

	public SanPhamEntity laySanPhamTheoMa(String maSp) {
		Session session = factory.getCurrentSession();
		String hql = "FROM SanPhamEntity WHERE id = :maSp";
		Query query = session.createQuery(hql);
		query.setParameter("maSp", maSp);
		SanPhamEntity sp = (SanPhamEntity) query.uniqueResult();
		Hibernate.initialize(sp.getHinhAnhs());
		return sp;
	}

	public List<SanPhamEntity> laySanPhamCungLoai(LoaiSanPhamEntity loaiSanPham, String maSp) {
		Session session = factory.getCurrentSession();
		String hql = "FROM SanPhamEntity WHERE loaiSanPham = :loaiSanPham AND id != :maSp";
		Query query = session.createQuery(hql);
		query.setParameter("loaiSanPham", loaiSanPham);
		query.setParameter("maSp", maSp);
		query.setMaxResults(3);
		List<SanPhamEntity> sanPhamCungLoai = query.list();
		return sanPhamCungLoai;
	}
}