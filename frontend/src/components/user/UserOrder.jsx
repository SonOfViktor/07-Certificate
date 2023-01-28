import React from 'react';
import {nanoid} from '@reduxjs/toolkit';
import {ListItem, ListItemText} from '@mui/material';
import {calculateDaysLeft, calculateExpireDate} from './utils/dateUtils';
import styled from '@emotion/styled';

const GridListItem = styled(ListItem)({
  display: 'grid',
  gridTemplateColumns: 'minmax(150px, 200px) 60px 60px',
  gap: '5px',
  paddingRight: 0,
});

const UserOrder = ({payment, order}) => {
  return (
    <GridListItem key={nanoid()}>
      <ListItemText
        primary={order.certificateName}
        secondary={calculateExpireDate(
          payment.createdDate,
          order.duration
        ).toLocaleString()}
        primaryTypographyProps={{fontWeight: 500}}
      />
      <ListItemText primary={`$${order.cost}`} />
      <ListItemText
        primary={calculateDaysLeft(payment.createdDate, order.duration)}
      />
    </GridListItem>
  );
};

export default UserOrder;
