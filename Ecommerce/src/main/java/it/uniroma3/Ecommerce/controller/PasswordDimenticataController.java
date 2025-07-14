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

	@Autowired private SessionData sessiondata;
	@Autowired private UserService userService;
	@Autowired private JavaMailSender mailSender;

	@GetMapping("/password_dimenticata")
	public String showForgotPasswordForm(Model model) {
		model.addAttribute("TitoloPagina", "Password Dimenticata");
		return "password_dimenticata_form.html";
	}

	@PostMapping("/password_dimenticata")
	public String processoPasswordDimenticataForm(HttpServletRequest request, Model model) throws UserNotFoundException {
		String email = request.getParameter("email");
		String token = RandomString.make(45);
		try {
			this.userService.updateResetPassword(email,token);
			String resetPasswordLink = Utilita.getSiteUrl(request) + "/reimposta_password?token=" + token;
			sendEmail(email,resetPasswordLink);
			model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
			
		} catch(UserNotFoundException ex){
			model.addAttribute("error", ex.getMessage());
		} catch (UnsupportedEncodingException  | MessagingException e) {
			model.addAttribute("error", "errore nnell'invio della mail");
		} 
		return "password_dimenticata_form.html";
	}

	private void sendEmail(String email, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = this.mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("ContactEcommerce@gmail.com","Ecommerce support");
		helper.setTo(email);
		String subject = "questo e' il link per resettare la password!";
		String content = "<p>Ciao!</p>"
				+"<p>Hai chiesto di cambiare la password.</p>"
				+"<p>clicca il link qua sotto per reimpostarla!</p>"
				+"<p><a href=\"" + resetPasswordLink + "\">Cambia la mia password!</a></p>";
		helper.setSubject(subject);
		helper.setText(content,true);
		mailSender.send(message);
	}

	@GetMapping("/reimposta_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
	    User user = this.userService.getByResetPasswordToken(token);
	    model.addAttribute("token", token);
	     
	    if (user == null) {
	        model.addAttribute("message", "Invalid Token");
	        return "message";
	    }
	     
	    return "reset_password_form.html";
	}
	
	@PostMapping("/reimposta_password")
	public String processResetPassword(HttpServletRequest request, Model model) throws UserNotFoundException {
	    String token = request.getParameter("token");
	    String password = request.getParameter("password");
	     
	    User user= this.userService.getByResetPasswordToken(token);
	    model.addAttribute("title", "Reset your password");
	     
	    if (user == null) {
	        model.addAttribute("message", "Invalid Token");
	        return "message";
	    } else {           
	        this.userService.updatePassword(user.getEmail(), password);
	         
	        model.addAttribute("message", "You have successfully changed your password.");
	    }
	     
	    return "redirect:/login";
	}
	
}
