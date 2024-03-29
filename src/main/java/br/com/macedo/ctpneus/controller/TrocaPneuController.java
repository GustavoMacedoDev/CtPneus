package br.com.macedo.ctpneus.controller;

import br.com.macedo.ctpneus.entity.TrocaPneu;
import br.com.macedo.ctpneus.controller.util.JsfUtil;
import br.com.macedo.ctpneus.controller.util.JsfUtil.PersistAction;
import br.com.macedo.ctpneus.entity.Veiculo;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("trocaPneuController")
@SessionScoped
public class TrocaPneuController implements Serializable {

    @EJB
    private br.com.macedo.ctpneus.controller.TrocaPneuFacade ejbFacade;
    private List<TrocaPneu> items = null;
    private TrocaPneu trocaPneuSelecionado;
    private Veiculo veiculoSelecionado;
    
    public TrocaPneuController() {
    }

    public TrocaPneu getTrocaPneuSelecionado() {
        return trocaPneuSelecionado;
    }

    public void setTrocaPneuSelecionado(TrocaPneu trocaPneuSelecionado) {
        this.trocaPneuSelecionado = trocaPneuSelecionado;
    }

    public Veiculo getVeiculoSelecionado() {
        return veiculoSelecionado;
    }

    public void setVeiculoSelecionado(Veiculo veiculoSelecionado) {
        this.veiculoSelecionado = veiculoSelecionado;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TrocaPneuFacade getFacade() {
        return ejbFacade;
    }

    public TrocaPneu prepareCreate() {
        trocaPneuSelecionado = new TrocaPneu();
        initializeEmbeddableKey();
        return trocaPneuSelecionado;
    }
    
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TrocaPneuCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TrocaPneuUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TrocaPneuDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            trocaPneuSelecionado = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<TrocaPneu> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (trocaPneuSelecionado != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(trocaPneuSelecionado);
                } else {
                    getFacade().remove(trocaPneuSelecionado);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public TrocaPneu getTrocaPneu(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<TrocaPneu> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<TrocaPneu> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    @FacesConverter(forClass = TrocaPneu.class)
    public static class TrocaPneuControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TrocaPneuController controller = (TrocaPneuController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "trocaPneuController");
            return controller.getTrocaPneu(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TrocaPneu) {
                TrocaPneu o = (TrocaPneu) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), TrocaPneu.class.getName()});
                return null;
            }
        }

    }

}
