import {useTheme} from '@emotion/react';
import {CardMedia, Stack, useMediaQuery} from '@mui/material';
import {Link} from 'react-router-dom';
import LogoTypography from './LogoTypography';
import {logoImageProperty} from './const/headerConst';

const Logo = () => {
  const theme = useTheme();
  const isLargeScreen = useMediaQuery(theme.breakpoints.up('sm'));

  return (
    <Link to="/">
      <Stack direction="row">
        <LogoTypography>Certifi</LogoTypography>
        <CardMedia
          component="img"
          height={isLargeScreen ? '40' : '35'}
          image="/assets/cat-face.svg"
          alt="cat"
          sx={logoImageProperty}
        />
        <LogoTypography>e</LogoTypography>
      </Stack>
    </Link>
  );
};

export default Logo;
