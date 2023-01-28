import styled from '@emotion/styled';
import {Typography} from '@mui/material';

const CertificateInfoWrapper = styled('div')({
  '& h2': {
    fontSize: '18px',
    letterSpacing: '-0.5px',
    fontWeight: 700,
  },
  '& p': {
    display: '-webkit-box',
    width: '90%',
    marginTop: '7px',
    lineHeight: '15px',
    WebkitLineClamp: '2',
    WebkitBoxOrient: 'vertical',
    overflow: 'hidden',
  },
  '@media screen and (max-width: 800px)': {
    '&': {
      alignSelf: 'center',
    },
    '& p': {
      display: 'none',
    },
  },
  '@media screen and (max-width: 450px)': {
    '& h2': {
      fontSize: '16px',
    },
  },
});

const CertificateInfo = ({name, description}) => {
  return (
    <CertificateInfoWrapper>
      <Typography variant="h2">{name}</Typography>
      <Typography>{description}</Typography>
    </CertificateInfoWrapper>
  );
};

export default CertificateInfo;
