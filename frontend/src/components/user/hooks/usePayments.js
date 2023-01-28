import {useEffect, useState} from 'react';
import {useDispatch} from 'react-redux';
import {useSearchParams} from 'react-router-dom';
import {loadOrders, loadPayments} from '../../../store/payment/paymentSlice';

export const usePayments = user => {
  const dispatch = useDispatch();
  const [searchParams, setSearchParams] = useSearchParams();
  const [expanded, setExpanded] = useState('');

  const pageNumber = +searchParams.get('page') || 1;

  useEffect(() => {
    dispatch(loadPayments({id: user.id, pageNumber}));
  }, [dispatch, user, pageNumber]);

  const handleChangePage = (_, newPage) => {
    setSearchParams(prev => {
      if (newPage > 1) {
        prev.set('page', newPage);
      } else {
        prev.delete('page');
      }

      return prev;
    });
  };

  const handleAccordionExpand = payment => (_, isExpanded) => {
    if (isExpanded && !payment.orders) {
      dispatch(loadOrders({id: payment.id}));
    }
    setExpanded(isExpanded ? payment.id : '');
  };

  return {expanded, pageNumber, handleChangePage, handleAccordionExpand};
};
