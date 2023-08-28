package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.onlinebanking.core.dataaccess.config.HibernateSessionFactory;
import org.onlinebanking.core.dataaccess.dao.interfaces.PaymentInstrumentDAO;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentInstrumentDAOHibernateImpl extends DAOHibernateImpl<PaymentInstrument> implements PaymentInstrumentDAO {

    public PaymentInstrumentDAOHibernateImpl(@Autowired HibernateSessionFactory hibernateSessionFactory) {
        super(hibernateSessionFactory, PaymentInstrument.class);
    }
}
