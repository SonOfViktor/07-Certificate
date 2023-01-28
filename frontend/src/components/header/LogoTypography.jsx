import {useTheme} from '@emotion/react';
import {Typography, useMediaQuery} from '@mui/material';

const LogoTypography = ({children}) => {
  const theme = useTheme();
  const isLargeScreen = useMediaQuery(theme.breakpoints.up('sm'));

  return (
    <Typography
      fontFamily="Times New Roman, Times, serif"
      fontSize={isLargeScreen ? '35px' : '28px'}
      fontWeight="100"
      textTransform="uppercase"
      letterSpacing="-3px"
      lineHeight="normal"
      color="var(--primary)"
      sx={{
        textShadow: '2px 2px 2px rgba(0, 0, 0, 0.4)',
      }}>
      {children}
    </Typography>
  );
};

export default LogoTypography;
