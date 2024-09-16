/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;


/**
 *
 * @author franc
 */
@ManagedBean
@RequestScoped
public class IndexNavigationController {
    public String navegarAEditPais()
    {
        return "editPais";
    }
}

