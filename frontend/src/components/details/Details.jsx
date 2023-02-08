import {CardMedia, Stack, styled} from '@mui/material';
import CertificateInfo from './CertificateInfo';
import LoadingData from '../ui/LoadingData';
import {useCertificateDetails} from './hooks/useCertificateDetails';

const DetailsContainer = styled('section')({
  display: 'grid',
  gridTemplateColumns: '2.5fr minmax(270px, 1fr)',
  padding: '30px 40px',
  columnGap: '40px',
  rowGap: '30px',
  '@media screen and (max-width: 1100px)': {
    '&': {
      gridTemplateColumns: '1fr',
    },
  },
  '@media screen and (max-width: 500px)': {
    padding: '20px 10px',
  },
});

const Details = () => {
  const [status, currentCertificate] = useCertificateDetails();

  return (
    <>
      <LoadingData status={status} />
      {currentCertificate && (
        <DetailsContainer>
          <CardMedia
            component="img"
            height="300"
            image={`/api/v1/certificates/${currentCertificate.giftCertificateId}/image`}
            alt={currentCertificate.name}
            sx={{backgroundColor: '#c4c4c4'}}
          />
          <CertificateInfo {...currentCertificate} />
          <Stack>
            <h3>Item Detailed Description</h3>
            <p>{currentCertificate.description}</p>
          </Stack>
        </DetailsContainer>
      )}
    </>
  );
};

export default Details;
