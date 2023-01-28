import {Typography} from '@mui/material';
import React from 'react';
import BucketContainer from './BucketContainer';

const totalProps = {
  gridColumn: 'span 5',
  paddingRight: '60px',
  textAlign: 'end',
  fontSize: '18px',
  fontWeight: '600',
  '@media screen and (max-width: 450px)': {
    gridColumn: 'span 4',
    fontSize: '16px',
  },
};

const BucketTotal = ({total}) => {
  return (
    <BucketContainer total>
      <Typography sx={totalProps}>Total:</Typography>
      <Typography className="price">${total}</Typography>
    </BucketContainer>
  );
};

export default BucketTotal;
