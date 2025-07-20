package it.uniroma3.Ecommerce.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.Ecommerce.authentication.SessionData;
import it.uniroma3.Ecommerce.authentication.Utilita;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.repository.CredentialsRepository;
import it.uniroma3.Ecommerce.service.UserNotFoundException;
import it.uniroma3.Ecommerce.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;

@Controller
public class PasswordDimenticataController {

	@Autowired
	private SessionData sessiondata;

	@Autowired
	private UserService userService;

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * metodo per visualizzare il form per richiedere il reset della passworod
	 * 
	 * @return password_dimenticata_form.html
	 **/
	@GetMapping("/password_dimenticata")
	public String showForgotPasswordForm(Model model) {

		// aggiorno il titolo della pagina
		model.addAttribute("TitoloPagina", "Password Dimenticata");
		return "password_dimenticata_form.html";
	}

	/**
	 * metodo per processare l'invio dell'email di reset
	 **/
	@PostMapping("/password_dimenticata")
	public String processoPasswordDimenticataForm(HttpServletRequest request, Model model)
			throws UserNotFoundException {

		// leggo il parametro dell'email dal form
		String email = request.getParameter("email");

		// genero il token casuale lungo 45 caratteri
		String token = RandomString.make(45);
		try {

			// salvo il token sul DB collegato all'utente tramite l'email
			this.userService.updateResetPassword(email, token);

			// creo il link completo per il reset
			String resetPasswordLink = Utilita.getSiteUrl(request) + "/reimposta_password?token=" + token;

			// invio la mail usando JavaMailSender
			sendEmail(email, resetPasswordLink);

			// aggiungo al model un messaggio di conferma dell'utente
			model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

		} catch (UserNotFoundException ex) {
			// se l'email non esiste mosto l'errore nel form
			model.addAttribute("error", ex.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {

			// gestisce altri eventuali problemi con la email
			model.addAttribute("error", "errore nnell'invio della mail");
		}
		return "password_dimenticata_form.html";
	}

	/**
	 * metodo per inviare l'email di reset
	 * @param email dell'utente
	 * @param il token per recupeare la password
	 * @return email mandata
	 * */
	private void sendEmail(String email, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
		
		//creo il mesasggio MIME 
		MimeMessage message = this.mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		//visualizzo il mittente e il nome
		helper.setFrom("ContactEcommerce@gmail.com", "Ecommerce support");
		helper.setTo(email);
		
		//oggetto e corpo HTML
		String subject = "questo e' il link per resettare la password!";
		String content = "<p>Ciao!</p>" + "<p>Hai chiesto di cambiare la password.</p>"
				+ "<p>clicca il link qua sotto per reimpostarla!</p>" + "<p><a href=\"" + resetPasswordLink
				+ "\">Cambia la mia password!</a></p>";
		
		helper.setSubject(subject);
		helper.setText(content, true); //'true' mi indica che il testo è HTML
		
		//invio l'email
		mailSender.send(message);
	}

	/**
	 * metodo per visualizzare il form di reset se il token è valido 
	 * @param token
	 * @return pagina per il reset della password
	 * */
	@GetMapping("/reimposta_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
		
		//recupero l'utente associato a quel token
		User user = this.userService.getByResetPasswordToken(token);
		model.addAttribute("token", token);

		//verifico se effettivamente esiste l'utente
		if (user == null) {
			
			//token non valido o scaduto
			model.addAttribute("message", "Invalid Token");
			return "message";
		}
		//token valido allora mostro la pagina per fare il reset della password
		return "reset_password_form.html";
	}

	/**
	 * metodo che processa il form di reset della password
	 * @return pagina di logi (login.html) 
	 **/
	@PostMapping("/reimposta_password")
	public String processResetPassword(HttpServletRequest request, Model model) throws UserNotFoundException {
		
		//leggo il token e la nuova password dal form
		String token = request.getParameter("token");
		String password = request.getParameter("password");

		//trovo l'utente tramite il token
		User user = this.userService.getByResetPasswordToken(token);
		model.addAttribute("title", "Reset your password");

		if (user == null) {
			//token non valido o scaduto
			model.addAttribute("message", "Invalid Token");
			return "message";
		} else {
			
			//il token è valido e aggiorno la password
			this.userService.updatePassword(user.getEmail(), password);
			
			//aggiungo un messaggio di conferma
			model.addAttribute("message", "You have successfully changed your password.");
		}

		return "redirect:/login";
	}

}
