import {IconButton, Stack, Typography} from '@mui/material';
import LinkedInIcon from '@mui/icons-material/LinkedIn';
import GitHubIcon from '@mui/icons-material/GitHub';

const Footer = () => {
  return (
    <footer>
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Typography>Copyright © 2023 Fairy Company</Typography>
        <Stack direction="row" justifyContent="end" spacing={2}>
          <IconButton href="https://linkedin.com/in/maksim-silevich">
            <LinkedInIcon sx={{color: '#1c66c2'}} />
          </IconButton>
          <IconButton href="https://github.com/SonOfViktor/Module7_Frontend">
            <GitHubIcon sx={{color: 'black'}} />
          </IconButton>
        </Stack>
      </Stack>
    </footer>
  );
};

export default Footer;
