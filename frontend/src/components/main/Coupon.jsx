import React from 'react';
import {Card, CardActionArea, CardMedia} from '@mui/material';
import CouponCardContent from './CouponCardContent';
import {useNavigate} from 'react-router-dom';

const Coupon = ({certificate}) => {
  const {giftCertificateId: id, name} = certificate;
  const navigator = useNavigate();

  return (
    <Card sx={{width: '100%'}}>
      <CardActionArea
        onClick={() => navigator(`details/${id}`)}
        sx={{overflow: 'hidden'}}>
        <CardMedia
          component="img"
          sx={{
            backgroundColor: 'var(--color-bg2)',
            '&:hover': {
              transform: 'scale(1.2)',
              transition: 'transform 0.7s',
            },
          }}
          height="180"
          image={`/api/v1/certificates/${id}/image`}
          alt={name}
        />
      </CardActionArea>
      <CouponCardContent certificate={certificate} />
    </Card>
  );
};

export default Coupon;
