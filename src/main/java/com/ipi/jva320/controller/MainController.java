package com.ipi.jva320.controller;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;


@Controller
public class MainController {


    @Autowired
    private SalarieAideADomicileService salarieAideADomicileService;


    @GetMapping(name = "/")
    public String home(Model model) throws SalarieException {

        /*SalarieAideADomicile aide = new SalarieAideADomicile("Jeannette Dupontelle", LocalDate.of(2021, 7, 1), LocalDate.now(), 0, 0, 10, 1, 0);
        salarieAideADomicileService.creerSalarieAideADomicile(aide);*/


        model.addAttribute("salCounter", salarieAideADomicileService.countSalaries());
        return "home";
    }


    @GetMapping("/salaries/{id}")
    public String getEmployeById(@PathVariable Long id,Model model) {

        SalarieAideADomicile salarie = salarieAideADomicileService.getSalarie(id);
        if(salarie == null){
            return "redirect:/salaries/aide/new";
        }

        model.addAttribute("id", salarie.getId());
        model.addAttribute("nom", salarie.getNom());
        model.addAttribute("moisEnCours", salarie.getMoisEnCours());
        model.addAttribute("moisDebutContrat", salarie.getMoisDebutContrat());
        model.addAttribute("joursTravaillesAnneeNMoins1", salarie.getJoursTravaillesAnneeNMoins1());
        model.addAttribute("congesPayesAcquisAnneeNMoins1", salarie.getCongesPayesAcquisAnneeNMoins1());
        model.addAttribute("congesPayesPrisAnneeNMoins1", salarie.getCongesPayesRestantAnneeNMoins1());
        model.addAttribute("joursTravaillesAnneeN", salarie.getJoursTravaillesAnneeN());
        model.addAttribute("congesPayesAcquisAnneeN", salarie.getCongesPayesAcquisAnneeN());

        model.addAttribute("salCounter", salarieAideADomicileService.countSalaries());

        return "detail_Salarie";
    }

    @GetMapping("/salaries/aide/new")
    public String createEmploye(Model model) {

        model.addAttribute("id", "");
        model.addAttribute("nom", "");
        model.addAttribute("moisEnCours", "");
        model.addAttribute("moisDebutContrat", "");
        model.addAttribute("joursTravaillesAnneeNMoins1", "");
        model.addAttribute("congesPayesAcquisAnneeNMoins1", "");
        model.addAttribute("congesPayesPrisAnneeNMoins1", "");
        model.addAttribute("joursTravaillesAnneeN", "");
        model.addAttribute("congesPayesAcquisAnneeN", "");

        model.addAttribute("salCounter", salarieAideADomicileService.countSalaries());

        return "detail_Salarie";
    }


    @PostMapping("/salaries/save")
    public String saveEmploye(@RequestParam("nom") String nom,@RequestParam("moisEnCours") String moisEnCours,@RequestParam("moisDebutContrat") String moisDebutContrat, @RequestParam("joursTravaillesAnneeN") Long joursTravaillesAnneeN, @RequestParam("congesPayesAcquisAnneeN") Long congesPayesAcquisAnneeN, @RequestParam("joursTravaillesAnneeNMoins1") Long joursTravaillesAnneeNMoins1, @RequestParam("congesPayesAcquisAnneeNMoins1") Long congesPayesAcquisAnneeNMoins1, @RequestParam("congesPayesPrisAnneeNMoins1") Long congesPayesPrisAnneeNMoins1) throws SalarieException {

        SalarieAideADomicile aide = new SalarieAideADomicile(nom, LocalDate.parse(moisEnCours), LocalDate.parse(moisDebutContrat), joursTravaillesAnneeN, congesPayesAcquisAnneeN, joursTravaillesAnneeNMoins1, congesPayesAcquisAnneeNMoins1, congesPayesPrisAnneeNMoins1);
        salarieAideADomicileService.creerSalarieAideADomicile(aide);
        return "redirect:/salaries/" + aide.getId();
    }

    @GetMapping("/salaries/{id}/delete")
    public String deleteEmploye(@PathVariable Long id) throws SalarieException {

        salarieAideADomicileService.deleteSalarieAideADomicile(id);

        return "redirect:/salaries/" + (id-1);
    }


    @GetMapping("/salaries")
    public String ListSalaries(Model model, @RequestParam(value = "matricule",required = false) String nom, @RequestParam(value = "page",required = false,defaultValue = "0") String page,@RequestParam(value = "size",required = false,defaultValue = "5") String size) throws SalarieException {



        if(nom != null){
            List<SalarieAideADomicile> aide = salarieAideADomicileService.getSalaries(nom);
            if(aide.stream().count() == 0){
                return "404";
            }
            return "redirect:/salaries/" + aide.get(0).getId();
        }

        if(page == null){
            page = "0";
        }

        if(size == null){
            size = "5";
        }

        List<SalarieAideADomicile> users = salarieAideADomicileService.getSalaries();

        model.addAttribute("users",getPage(users,Integer.parseInt(page),Integer.parseInt(size)));
        model.addAttribute("size",Integer.parseInt(size));
        model.addAttribute("pageN",Integer.parseInt(page));
        model.addAttribute("NextPage",Integer.parseInt(page)+1);
        model.addAttribute("BackPage",Integer.parseInt(page)-1);
        model.addAttribute("totalSalarie",users.stream().count());




        return "list";
    }


    public static List<SalarieAideADomicile> getPage(List<SalarieAideADomicile> list, int page, int elementsPerPage) {
        int startIndex = page * elementsPerPage;
        int endIndex = Math.min(startIndex + elementsPerPage, list.size());
        return list.subList(startIndex, endIndex);
    }






}
