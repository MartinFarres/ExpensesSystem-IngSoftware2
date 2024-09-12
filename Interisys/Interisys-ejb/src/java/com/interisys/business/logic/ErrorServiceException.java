/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import javax.ejb.ApplicationException;

/**
 *
 * @author franc
 */
@ApplicationException(rollback=true)
public class ErrorServiceException extends Exception {

    public ErrorServiceException() {
    }


    public ErrorServiceException(String msg) {
        super(msg);
    }
}
