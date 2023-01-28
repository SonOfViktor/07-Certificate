import {Chip, Stack, styled, Typography} from '@mui/material';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import FavoriteIcon from '@mui/icons-material/Favorite';
import {useFavorite} from '../favorite/hook/useFavorite';
import {useDispatch} from 'react-redux';
import {addBucketCertificate} from '../../store/bucket/bucketSlice';

const CertificateInfoWrapper = styled('div')({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'space-between',
  padding: '10px',
  backgroundColor: 'white',
  '& h2': {
    letterSpacing: '-0.5px',
    marginBottom: '5px',
  },
  '& svg:hover': {
    color: 'var(--primary)',
  },
});

const CertificateInfo = props => {
  const {name, category, duration, price, tags} = props;
  const [isFavorite, toggleFavorite] = useFavorite(props);
  const dispatch = useDispatch();

  return (
    <CertificateInfoWrapper>
      <Stack>
        <Stack direction="row" justifyContent="space-between">
          <Typography component="h2" fontSize="18px" fontWeight="700">
            {name}
          </Typography>
          <FavoriteIcon
            color={isFavorite ? 'secondary' : 'action'}
            onClick={toggleFavorite}
          />
        </Stack>
        <Typography component="h4" fontSize="14px" fontWeight="500">
          {category}
        </Typography>
        <Stack
          direction="row"
          mt={1}
          flexWrap="wrap"
          gap={1}
          height="90px"
          alignContent="start"
          overflow="auto">
          {tags.map(({name: tag}) => (
            <Chip key={tag} label={tag} size="small" variant="outlined" />
          ))}
        </Stack>
        <Stack my={2}>
          <Typography
            component="span"
            fontSize="12px"
            fontWeight="500"
            color="action">
            Expiration Info
          </Typography>
          <Typography fontWeight="600" component="span">
            {duration} days
          </Typography>
        </Stack>
      </Stack>
      <Stack direction="row" justifyContent="space-between">
        <Typography fontWeight="700" fontSize="18px" component="span">
          ${price}
        </Typography>
        <ShoppingCartIcon
          color="action"
          onClick={() => dispatch(addBucketCertificate(props))}
        />
      </Stack>
    </CertificateInfoWrapper>
  );
};

export default CertificateInfo;
