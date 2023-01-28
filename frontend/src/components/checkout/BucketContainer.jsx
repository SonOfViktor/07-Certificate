import {styled} from '@mui/material';

const BucketContainer = styled('div')(({total}) => ({
  display: 'grid',
  gridTemplateColumns: '70px auto 50px 50px 50px 70px',
  gap: '15px',
  marginTop: total && '15px',
  padding: total && '20px 0',
  borderTop: total && '1px solid #c4c4c4',
  '& button': {
    alignSelf: 'center',
    justifySelf: 'center',
  },
  '& .price': {
    alignSelf: 'center',
    justifySelf: 'center',
    fontSize: 18,
    fontWeight: 600,
  },
  '@media screen and (max-width: 450px)': {
    gridTemplateColumns: 'minmax(140px, auto) 30px 30px 30px 70px',
    gap: '10px',
    '& svg': {
      fontSize: '1.5rem',
    },
    '& .price': {
      fontSize: 16,
      fontWeight: 600,
    },
    '& img': {
      display: 'none',
    },
  },
}));

export default BucketContainer;
