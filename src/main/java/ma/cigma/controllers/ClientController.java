package ma.cigma.controllers;

import ma.cigma.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ClientController {

    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = {"/","/clients"})
    public String home(Model model,@ModelAttribute Client client){
        model.addAttribute("client",client == null ? new Client() : client);
        List<Client> clients = restTemplate.getForObject(apiUrl + "/client/all",List.class);
        model.addAttribute("clients",clients);
        return "index-client";
    }

    @PostMapping(value = "/add-client")
    public String addClient(Model model, @ModelAttribute Client client){
        restTemplate.postForObject(apiUrl + "/client/create",client,Client.class);
        return "redirect:/clients";
    }

    @GetMapping(value = "/delete-client/{id}")
    public String deleteClient(Model model, @PathVariable long id){
        restTemplate.delete(apiUrl + "/client/delete/"+id);
        return "redirect:/clients";
    }

    @GetMapping(value = "/show-client/{id}")
    public String show(Model model, @PathVariable long id, RedirectAttributes ra){

        Client clt = restTemplate.getForObject(apiUrl + "/client/"+id,Client.class);
        ra.addFlashAttribute("client",clt);
        return "redirect:/clients";
    }
}
