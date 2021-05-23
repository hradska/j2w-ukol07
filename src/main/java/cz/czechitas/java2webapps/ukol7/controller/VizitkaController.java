package cz.czechitas.java2webapps.ukol7.controller;

import cz.czechitas.java2webapps.ukol7.entity.Vizitka;
import cz.czechitas.java2webapps.ukol7.repository.VizitkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class VizitkaController {

    private final VizitkaRepository repository;

    @Autowired
    public VizitkaController(VizitkaRepository repository) {
        this.repository = repository;
    }

    @InitBinder
    public void nullStringBinding(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/")
    public ModelAndView seznam() {
        ModelAndView drzakVizitek = new ModelAndView("seznam");
        drzakVizitek.addObject("seznam", repository.findAll());
        return drzakVizitek;
    }

    @GetMapping(path = "/{id}")
    public Object vizitka(@PathVariable int id) {
        ModelAndView drzakVizitka = new ModelAndView("vizitka");
        Optional<Vizitka> vizitka = repository.findById(id);
        if (vizitka.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        drzakVizitka.addObject("vizitka", vizitka.get());
        return drzakVizitka;
    }

    @GetMapping("/nova")
    public ModelAndView zobrazNova() {
        ModelAndView modelAndView = new ModelAndView("formular");
        modelAndView.addObject("form", new Vizitka());
        return modelAndView;
    }

    @PostMapping("/nova")
    public String ulozVizitku(@ModelAttribute("form") @Valid Vizitka vizitka, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formular";
        }
        repository.save(vizitka);
        return "redirect:/";
    }

    @PostMapping("/smazat")
    public String smazVizitku(int id) {
        repository.deleteById(id);
        return "redirect:/";
    }
}
