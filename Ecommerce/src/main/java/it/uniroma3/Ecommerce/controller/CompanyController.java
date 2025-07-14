package it.uniroma3.Ecommerce.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.Ecommerce.authentication.ProductNotFoundException;
import it.uniroma3.Ecommerce.authentication.SessionData;
import it.uniroma3.Ecommerce.model.Company;
import it.uniroma3.Ecommerce.model.Product;
import it.uniroma3.Ecommerce.model.ProductDto;
import it.uniroma3.Ecommerce.repository.CompanyRepository;
import it.uniroma3.Ecommerce.repository.ProductRepository;
import it.uniroma3.Ecommerce.service.CompanyService;
import it.uniroma3.Ecommerce.service.ProductService;
import jakarta.validation.Valid;


/*classe che si occupa di gestire le operazioni fatte dall'azienda all'interno della sua area privata*/

@Controller
public class CompanyController {

	@Autowired
	CompanyService companyService;
	
	@Autowired 
	ProductRepository productRepository;
	
	@Autowired
	ProductService productService;
	
	@Autowired 
	private SessionData sessionData;

	private Company azienda;
	
	//per visualizzare l'home page dell'azienda
	@GetMapping("/company/indexCompany")
	public String showHomePageCompany(Model model){
		model.addAttribute("userDetails", this.sessionData.getUserDetails());
		return "/company/indexCompany.html";
	}
	
	
	//quando voglio visualizzare la lista dei prodotti inseriti
	@GetMapping("/company/products")
	public String showProductList(Model model) {
		List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));	//per visualizzare la lista
		//dei prodotti ordinata per id, in modo che i prodotti più recenti siano in alto
		model.addAttribute("userDetails", this.sessionData.getUserDetails());
		model.addAttribute("products", products); 
		return "company/products.html";
	}
	
	
	/*devo creare un metodo che permette all'utente di accedere al form che gli permetterà di aggiungere nuovi prodotti*/
	@GetMapping("/company/showCreateProduct")
	public String showCratePage(Model model) {
		
		ProductDto productDto = new ProductDto(); 
		model.addAttribute("productDto", productDto); 	//tipo di oggetto
		return "/company/newProduct.html";
	}
	
	
	/*dobbiamo creare un metodo che ci permette di creare un nuovo prodotto*/
	@PostMapping("/company/showCreateProduct")
	public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {
		
		//verifico manualmente se è stata inserita l'immagine
		if(productDto.getImageFile().isEmpty()) {
			result.addError(new FieldError("productDto", "imageFile", "l'immagine è obbligatoria"));
		}
		
		//se ci sono errori
		if(result.hasErrors()) {
			return "/company/newProduct.html";
		}
		
		/*dobbiamo ora salvare il prodotto creato nel database*/
		MultipartFile image = productDto.getImageFile(); 
		Date createdAt = new Date();
		
		String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
		
		try {
			String uploadDir = "public/images/"; 	//directory dove salvare le immagini
			Path uploadPath = Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			try(InputStream inputStream = image.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
			}
			
		}catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		/*passo i valori di productDto a product in modo da creare l'oggetto prodotto vero e proprio*/
		this.azienda = this.companyService.creaCompany();
		Product product = new Product(); 
		product.setName(productDto.getName());
		product.setBrand(productDto.getBrand());
		product.setCategory(productDto.getCategory());
		product.setPrice(productDto.getPrice());
		product.setDescription(productDto.getDescription());
		product.setCreatedAt(createdAt);
		product.setImageFileName(storageFileName);
		this.azienda.addProdotto(product);
		productService.save(product);	//ho effettivamente salvato il prodotto creato
		
		return "redirect:/company/products";	//ridireziono l'azienda
	}
	
	
	/**@param modello come dati da passare alla pagina di modifica del prodotto
	 * @param id del prodotto da modificare
	 * @return la pagina dove posso modificare il prodotto
	 **/
	
	@GetMapping("/company/edit")
	public String showEditPage(Model model,@RequestParam Integer id) {
		//Product product = productRepository.findById(id).get();	
		try {
			Product product = productRepository.findById(id).get();
			model.addAttribute("product",product);
			
			
			ProductDto productDto = new ProductDto();
			productDto.setName(product.getName());
			productDto.setBrand(product.getBrand()); 
			productDto.setCategory(product.getCategory()); 
			productDto.setPrice(product.getPrice()); 
			productDto.setDescription(product.getDescription());
			
			model.addAttribute("productDto", productDto);
			
		}catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage()); 
			return "redirect:/company/products.html";
		}
		
		return "/company/editProduct.html";
	}
	
	
	/*che cosa faccio una volta che ho modificato il prodotto*/
	@PostMapping("/product/edit")
	public String updateProduct(Model model, @RequestParam Integer id, @Valid @ModelAttribute ProductDto productDto,
			BindingResult result) {
		
		
		//prima di tutto ci dobbiamo connettere al database
		try {
			
			Product product = productRepository.findById(id).get();
			model.addAttribute("product", product);
			
			//verifichiamo ora se i parametri del form sono validi o no
			if(result.hasErrors()) {
				return "company/indexCompany.html"; //se ci sono errori torno alla pagina iniziale per l'azienda
			}
			
			//se non ci sono errori verifico se è stata cambiata l'immagine del prodotto
			if(!productDto.getImageFile().isEmpty()) {
				
				//cancello la vecchia immagine
				String uploadDir = "public/images/";	//directory dell'immagine
				Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
				
				try {
					Files.delete(oldImagePath); 
				}catch(Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}
				
				//salvo il nuovo file 
				MultipartFile image = productDto.getImageFile();	
				Date createdAt = new Date();
				String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
				
				try(InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
				}
				product.setImageFileName(storageFileName);
			}
			
			//dobbiamo aggiornare i valori
			product.setName(productDto.getName());
			product.setBrand(productDto.getBrand());
			product.setCategory(productDto.getCategory());
			product.setPrice(productDto.getPrice());
			product.setDescription(productDto.getDescription());
			
			productRepository.save(product);
			//productService.save(product);
			
		}catch(Exception ex){
			System.out.println("Exception: " + ex.getMessage());
		}
		
		return "redirect:/company/products";
	}
	
	
	
	/*per cancellare un prodotto*/
	@GetMapping("/company/delete")
	public String deleteProduct(@RequestParam Integer id) {
		
		
		try {
			Product product = productRepository.findById(id).get();
			
			//elimino l'immagine del prodotto 
			Path imagePath = Paths.get("public/images/" + product.getImageFileName());
			
			try {
				Files.delete(imagePath);
				
			}catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
			
			//elimino il prodotto vero e proprio 
			productRepository.delete(product);
			
		}catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		return "redirect:/company/products";
	}
	
	

}
