package com.example.tads.eaj.ufrn.jogostab.controllers;


import com.example.tads.eaj.ufrn.jogostab.models.JogosTabuleiro;
import com.example.tads.eaj.ufrn.jogostab.services.FileStorageService;
import com.example.tads.eaj.ufrn.jogostab.services.JogoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Controller
public class JogoController {

    JogoServices service;
    FileStorageService storageService;


    @Autowired
    public void setFileStorageService(FileStorageService fileStorageService) {
        this.storageService = fileStorageService;
    }

    @Autowired
    public void setService(JogoServices service) {
        this.service = service;
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String getHome(Model model, HttpServletResponse response){
        var listajogos = service.findall();
        model.addAttribute("listajogos", listajogos);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy|HH:mm:ss");
        Cookie cookie = new Cookie("Visita", dtf.format(LocalDateTime.now()));
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);

        return "index";
    }
    @RequestMapping("/cadastro")
    public String getFormCadastro(Model model){
        JogosTabuleiro jt = new JogosTabuleiro();
        model.addAttribute("jogot", jt);
        return "cadastro";
    }
    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public String doSalvar(@ModelAttribute @Valid JogosTabuleiro jogosTabuleiro, Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        if (errors.hasErrors()){
            return "cadastro";
        }else{
			jogosTabuleiro.setImagemUri(file.getOriginalFilename());
            service.save(jogosTabuleiro);
            storageService.save(file);

            redirectAttributes.addAttribute("msg", "Cadastro realizado com sucesso");
            return "redirect:/";
        }
    }
    @RequestMapping("/vercarrinho")
    public ModelAndView getCarrinho(HttpServletRequest request, Model model){
        HttpSession session= request.getSession();
        if(session.getAttribute("carrinho")==null){
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            return modelAndView;
         }else{
            ModelAndView modelAndView = new ModelAndView("carrinho");
            modelAndView.addObject("carrinho",session.getAttribute("carrinho"));
            return modelAndView;
        }

    }
    @RequestMapping("/adicionarCarrinho/{id}")
    public String adCarrinho(HttpServletRequest request,RedirectAttributes redirectAttributes,@PathVariable(name = "id") Long id) throws IOException, ServletException {
        HttpSession session =request.getSession();
        var cont=0;
        if(session.getAttribute("carrinho") ==null){
            session.setAttribute("carrinho", new ArrayList<JogosTabuleiro>());
        }
        JogosTabuleiro jogosTabuleiro =service.findById(id);
        ArrayList<JogosTabuleiro> carrinho=(ArrayList<JogosTabuleiro>)session.getAttribute("carrinho");
        carrinho.add(jogosTabuleiro);
        cont++;
        redirectAttributes.addAttribute("msg","O Jogo adicionado no carrinho foi:"+jogosTabuleiro.getNome());
        redirectAttributes.addAttribute("cont", cont);
        return "redirect:/";

    }
    @RequestMapping(value = {"/admin" }, method = RequestMethod.GET)
    public String listaex(Model model){
        var listajogosa = service.listAll();
        model.addAttribute("listajogos", listajogosa);
        return "admin";
    }
    @RequestMapping("/editar/{id}")
    public ModelAndView getFormEdicao(@PathVariable(name = "id") Long id){
        ModelAndView modelAndView = new ModelAndView("editar");
        JogosTabuleiro jogosTabuleiro = this.service.findById(id);
        modelAndView.addObject("jogosTabuleiro", jogosTabuleiro);
        return modelAndView;
    }
    @RequestMapping("/deletar/{id}")
    public String doDelete(@PathVariable(name = "id") Long id){
        service.delete(id);
        return "redirect:/";
    }

    @RequestMapping(value = "/finalizarcompras")
    public String finishCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }

}
