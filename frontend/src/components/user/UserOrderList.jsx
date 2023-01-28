import {Button, List, ListItem} from '@mui/material';
import {nanoid} from '@reduxjs/toolkit';
import React from 'react';
import {useDispatch} from 'react-redux';
import {loadOrders} from '../../store/payment/paymentSlice';
import LoadingAlert from '../ui/LoadingAlert';
import LoadingData from '../ui/LoadingData';
import UserOrder from './UserOrder';

const UserOrderList = ({payment}) => {
  const dispatch = useDispatch();

  const handleNextOrders = () => {
    dispatch(
      loadOrders({id: payment.id, pageNumber: payment.ordersPage?.number + 1})
    );
  };

  return (
    <List>
      {payment.orders &&
        payment.orders.map(order => (
          <UserOrder key={nanoid()} payment={payment} order={order} />
        ))}
      <LoadingData status={payment.ordersStatus} />
      <LoadingAlert status={payment.ordersStatus} error={payment.ordersError} />

      {payment.ordersStatus === 'idle' &&
        payment?.ordersPage?.number !== payment?.ordersPage?.totalPages - 1 && (
          <ListItem>
            <Button
              variant="contained"
              sx={{color: 'white', marginLeft: 'auto', marginRight: 'auto'}}
              onClick={handleNextOrders}>
              More
            </Button>
          </ListItem>
        )}
    </List>
  );
};

export default UserOrderList;
