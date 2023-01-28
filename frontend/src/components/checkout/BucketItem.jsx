import {CardMedia, Typography} from '@mui/material';
import React from 'react';
import HoverIconButton from '../ui/HoverIconButton';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import CertificateInfo from '../favorite/CertificateInfo';
import {useDispatch} from 'react-redux';
import {
  addBucketCertificate,
  deleteBucketCertificate,
} from '../../store/bucket/bucketSlice';

const BucketItem = ({certificate}) => {
  const {giftCertificateId: id, name, description, amount, price} = certificate;
  const dispatch = useDispatch();

  return (
    <>
      <CardMedia
        component="img"
        height="70"
        width="70"
        image={`/api/v1/certificates/${id}/image`}
        alt={name}
        sx={{backgroundColor: '#c4c4c4'}}
      />
      <CertificateInfo name={name} description={description} />
      <HoverIconButton onClick={() => dispatch(deleteBucketCertificate(id))}>
        <RemoveIcon />
      </HoverIconButton>
      <Typography alignSelf="center" justifySelf="center" fontSize={16}>
        {amount}
      </Typography>
      <HoverIconButton
        onClick={() => dispatch(addBucketCertificate(certificate))}>
        <AddIcon />
      </HoverIconButton>
      <Typography className="price">${price}</Typography>
    </>
  );
};

export default BucketItem;
