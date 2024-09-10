/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.interisys.business.persistence;

import javax.ejb.ApplicationException;

/**
 *
 * @author Dell
 */
@ApplicationException(rollback=false)
public class ErrorDAOException extends Exception {

    /**
     * Creates a new instance of <code>PaisException</code> without detail message.
     */
    public ErrorDAOException() {
    }


    /**
     * Constructs an instance of <code>PaisException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ErrorDAOException(String msg) {
        super(msg);
    }
}
