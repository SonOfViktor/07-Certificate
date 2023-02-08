import {CardMedia, Typography, useMediaQuery} from '@mui/material';
import HoverIconButton from '../ui/HoverIconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import CertificateInfo from './CertificateInfo';
import {useDispatch} from 'react-redux';
import {deleteFavoriteCertificate} from '../../store/favorite/favoriteSlice';
import {addBucketCertificate} from '../../store/bucket/bucketSlice';

const FavoriteItem = ({certificate}) => {
  const width450 = useMediaQuery('(max-width:450px)');
  const {giftCertificateId: id, name, description, price} = certificate;
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
      <Typography
        alignSelf="center"
        justifySelf="center"
        fontSize={width450 ? 16 : 18}
        fontWeight={600}>
        ${price}
      </Typography>
      <HoverIconButton
        onClick={() => dispatch(addBucketCertificate(certificate))}>
        <ShoppingCartIcon />
      </HoverIconButton>
      <HoverIconButton onClick={() => dispatch(deleteFavoriteCertificate(id))}>
        <DeleteIcon />
      </HoverIconButton>
    </>
  );
};

export default FavoriteItem;
